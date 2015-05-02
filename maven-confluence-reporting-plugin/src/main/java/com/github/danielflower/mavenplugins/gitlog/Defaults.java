package com.github.danielflower.mavenplugins.gitlog;

import com.github.danielflower.mavenplugins.gitlog.filters.CommitFilter;
import com.github.danielflower.mavenplugins.gitlog.filters.DuplicateCommitMessageFilter;
import com.github.danielflower.mavenplugins.gitlog.filters.MavenReleasePluginMessageFilter;
import com.github.danielflower.mavenplugins.gitlog.filters.MergeCommitFilter;

import java.util.*;

class Defaults {
	public static final List<CommitFilter> DEFAULT_COMMIT_FILTERS = Arrays.asList(
			new MavenReleasePluginMessageFilter(),
			new MergeCommitFilter(),
			new DuplicateCommitMessageFilter()
	);
	
	public static final List<CommitFilter> COMMIT_FILTERS;
	
	static {
		COMMIT_FILTERS = new ArrayList<CommitFilter>();
		COMMIT_FILTERS.addAll(DEFAULT_COMMIT_FILTERS);
		
		Iterator<CommitFilter> it = ServiceLoader.load(CommitFilter.class).iterator();
		while (it.hasNext()){
			COMMIT_FILTERS.add(it.next());
		}
	}
}
