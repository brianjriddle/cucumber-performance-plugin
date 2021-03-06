package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import com.castlemon.jenkins.performance.CucumberProjectAction;
import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;

import hudson.model.AbstractProject;

public class CucumberPerfUtilsTest {

    private TestUtils testUtils = new TestUtils();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testWriteSummaryToDisk() {
        ProjectSummary summary = new ProjectSummary();
        Assert.assertTrue(CucumberPerfUtils.writeSummaryToDisk(summary,
                testFolder.getRoot()));
        Assert.assertEquals(1, testFolder.getRoot().listFiles().length);
    }

    @Test
    public void testReadSummaryFromDisk() throws Exception {
        File f = FileUtils.toFile(this.getClass().getResource("/cukeperf.xml"));
        ProjectSummary summary = CucumberPerfUtils.readSummaryFromDisk(f
                .getParentFile());
        Assert.assertEquals(11, summary.getOverallSummary().getPassedBuilds());
        Assert.assertEquals(2, summary.getFeatureSummaries().size());
        Assert.assertEquals(11, summary.getOverallSummary().getEntries().size());
    }

    @Test
    public void testReadSummaryFromDiskException() throws Exception {
        File f = FileUtils
                .toFile(this.getClass().getResource("/notexists.xml"));
        Assert.assertNull(CucumberPerfUtils.readSummaryFromDisk(null));
    }

    @Test
    public void testGetRelevantSummariesMultiple() {
        Map<String, Summary> summaries = new HashMap<String, Summary>();
        Summary summary1 = new Summary();
        summary1.setId("fred");
        summary1.setSeniorId("sen1");
        summary1.setOrder(2);
        summaries.put(summary1.getSeniorId() + summary1.getId(), summary1);
        Summary summary2 = new Summary();
        summary2.setId("bill");
        summary2.setSeniorId("senZ");
        summaries.put(summary2.getSeniorId() + summary2.getId(), summary2);
        Summary summary3 = new Summary();
        summary3.setId("george");
        summary3.setSeniorId("sen1");
        summary3.setOrder(1);
        summaries.put(summary3.getSeniorId() + summary3.getId(), summary3);
        List<Summary> output = CucumberPerfUtils.getRelevantSummaries(
                summaries, "sen1");
        Assert.assertEquals(2, output.size());
        Assert.assertEquals("george", output.get(0).getId());
        Assert.assertEquals("fred", output.get(1).getId());
    }

    @Test
    public void testGetRelevantSummariesNone() {
        Map<String, Summary> summaries = new HashMap<String, Summary>();
        Summary summary1 = new Summary();
        summary1.setId("fred");
        summary1.setSeniorId("senA");
        summaries.put(summary1.getSeniorId() + summary1.getId(), summary1);
        Summary summary2 = new Summary();
        summary2.setId("bill");
        summary2.setSeniorId("senZ");
        summaries.put(summary2.getSeniorId() + summary2.getId(), summary2);
        Summary summary3 = new Summary();
        summary3.setId("george");
        summary3.setSeniorId("senC");
        summaries.put(summary3.getSeniorId() + summary3.getId(), summary3);
        List<Summary> output = CucumberPerfUtils.getRelevantSummaries(
                summaries, "sen1");
        Assert.assertEquals(0, output.size());
    }

    @Test
    public void testFormatDuration() {
        Assert.assertEquals("30 mins 30 secs 900 ms",
                CucumberPerfUtils.formatDuration(1830900900300l));
    }

    @Test
    public void testFormatDurationZeroSeconds() {
        Assert.assertEquals("2 ms",
                CucumberPerfUtils.formatDuration(0000010000000l));
    }

    @Test
    public void testFormatDurationOneSecond() {
        Assert.assertEquals("1 sec 73 ms",
                CucumberPerfUtils.formatDuration(0010000000000l));
    }

    @Test
    public void testGetDataSingle() throws IOException {
        String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
        List<Feature> features = CucumberPerfUtils.getData(jsonString);
        Assert.assertEquals(2, features.size());
    }

    @Test
    public void testGetDataSingleJsonParseException() throws IOException {
        String jsonString = "fred";
        List<Feature> features = CucumberPerfUtils.getData(jsonString);
        Assert.assertEquals(0, features.size());
    }

    @Test
    public void testGetDataSingleJsonMappingException() throws IOException {
        String jsonString = "[1,4{}";
        List<Feature> features = CucumberPerfUtils.getData(jsonString);
        Assert.assertEquals(0, features.size());
    }

    @Test
    public void testFindJsonFiles() throws IOException {
        File f = FileUtils.toFile(this.getClass().getResource(
                "/cucumber-success.json"));
        String[] results = CucumberPerfUtils.findJsonFiles(f.getParentFile(),
                ".json");
        // the figure below is a count of the *.json files in src/test/resources
        Assert.assertEquals(8, results.length);

    }

    @Test
    public void testGetDataMultiple() throws IOException {
        String[] jsonReportFiles = { "cucumber-success.json",
                "cucumber-failure.json" };
        File f = FileUtils.toFile(this.getClass().getResource(
                "/cucumber-success.json"));
        List<Feature> features = CucumberPerfUtils.getData(jsonReportFiles,
                f.getParentFile());
        Assert.assertEquals(4, features.size());
    }

    @Test
    public void testGetDataMultipleIOException() throws IOException {
        String[] jsonReportFiles = { "cucumber-success.json", "nonexist.json" };
        File f = FileUtils.toFile(this.getClass().getResource(
                "/cucumber-success.json"));
        List<Feature> features = CucumberPerfUtils.getData(jsonReportFiles,
                f.getParentFile());
        Assert.assertEquals(2, features.size());
    }

    @Test
    public void getRelevantSummariesWorksWithOutSeniorIdDefined() throws IOException {
        testFolder.newFolder("cucumber-perf-reports");
        FileUtils.copyFile(
                FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
                testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
        AbstractProject project = Mockito.mock(AbstractProject.class);
        Mockito.when(project.getLastCompletedBuild()).thenReturn(null);
        Mockito.when(project.getRootDir()).thenReturn(testFolder.getRoot());
        CucumberProjectAction tempCucumberProjectAction = new CucumberProjectAction(
                project, 20);
        ProjectSummary summary = tempCucumberProjectAction.getProjectSummary();
        CucumberPerfUtils.getRelevantSummaries(summary.getStepSummaries(), "");
    }

}
