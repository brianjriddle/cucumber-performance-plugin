package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class Summary {

	private String id;

	private String name;

	private List<PerformanceEntry> entries;

	private long shortestDuration = Long.MAX_VALUE;

	private long longestDuration;

	private long averageDuration;

	private int totalBuilds;

	private int passedBuilds;

	private int failedBuilds;

	private int reportedBuilds;

	private int passedSteps;

	private int failedSteps;

	private int skippedSteps;

	private String seniorId;

	private int order;

	private int numberOfSubItems;

	private final String pageLink;

	// only used for steps
	private String keyword;

	private List<List<String>> rows;

	public Summary() {
		this.pageLink = RandomStringUtils.randomAlphabetic(5);
	}

	public String getPageLink() {
		return pageLink + ".html";
	}

	public boolean hasRows() {
		return (rows != null);
	}

	public long calculateAverageDuration() {
		long count = 0l;
		long duration = 0l;
		for (PerformanceEntry entry : entries) {
			if (entry.isPassed()) {
				duration += entry.getElapsedTime();
				count++;
			}
		}
		if (count > 0) {
			averageDuration = duration / count;
			return averageDuration;
		}
		return 0l;
	}

	public String getFormattedAverageDuration() {
		calculateAverageDuration();
		return CucumberPerfUtils.formatDuration(averageDuration);
	}

	public long getAverageInMilliseconds() {
		calculateAverageDuration();
		// return (averageDuration / 1000000000);
		return (averageDuration / 1000000);
	}

	public String getFormattedShortestDuration() {
		if (shortestDuration == Long.MAX_VALUE) {
			// field not updated, return 0
			return CucumberPerfUtils.formatDuration(0l);
		}
		return CucumberPerfUtils.formatDuration(shortestDuration);
	}

	public String getFormattedLongestDuration() {
		return CucumberPerfUtils.formatDuration(longestDuration);
	}

	public void addToPassedSteps(int passedSteps) {
		this.passedSteps += passedSteps;
	}

	public void addToSkippedSteps(int skippedSteps) {
		this.skippedSteps += skippedSteps;
	}

	public void addToFailedSteps(long failedSteps) {
		this.failedSteps += failedSteps;
	}

	public void incrementTotalBuilds() {
		this.totalBuilds++;
	}

	public void incrementPassedBuilds() {
		this.passedBuilds++;
	}

	public void incrementFailedBuilds() {
		this.failedBuilds++;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PerformanceEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PerformanceEntry> entries) {
		this.entries = entries;
	}

	public long getShortestDuration() {
		/*
		 * if (this.shortestDuration == Long.MAX_VALUE) { // field not updated,
		 * return 0 return 0l; }
		 */
		return this.shortestDuration;
	}

	public void setShortestDuration(long shortestDuration) {
		this.shortestDuration = shortestDuration;
	}

	public long getLongestDuration() {
		return longestDuration;
	}

	public void setLongestDuration(long longestDuration) {
		this.longestDuration = longestDuration;
	}

	public int getTotalBuilds() {
		return totalBuilds;
	}

	public void setTotalBuilds(int totalBuilds) {
		this.totalBuilds = totalBuilds;
	}

	public int getPassedBuilds() {
		return passedBuilds;
	}

	public void setPassedBuilds(int passedBuilds) {
		this.passedBuilds = passedBuilds;
	}

	public int getFailedBuilds() {
		return failedBuilds;
	}

	public void setFailedBuilds(int failedBuilds) {
		this.failedBuilds = failedBuilds;
	}

	public int getReportedBuilds() {
		return reportedBuilds;
	}

	public void setReportedBuilds(int reportedBuilds) {
		this.reportedBuilds = reportedBuilds;
	}

	public int getPassedSteps() {
		return passedSteps;
	}

	public void setPassedSteps(int passedSteps) {
		this.passedSteps = passedSteps;
	}

	public int getFailedSteps() {
		return failedSteps;
	}

	public void setFailedSteps(int failedSteps) {
		this.failedSteps = failedSteps;
	}

	public int getSkippedSteps() {
		return skippedSteps;
	}

	public void setSkippedSteps(int skippedSteps) {
		this.skippedSteps = skippedSteps;
	}

	public String getSeniorId() {
		return seniorId;
	}

	public void setSeniorId(String seniorId) {
		this.seniorId = seniorId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getNumberOfSubItems() {
		return numberOfSubItems;
	}

	public void setNumberOfSubItems(int numberOfSubItems) {
		this.numberOfSubItems = numberOfSubItems;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

}
