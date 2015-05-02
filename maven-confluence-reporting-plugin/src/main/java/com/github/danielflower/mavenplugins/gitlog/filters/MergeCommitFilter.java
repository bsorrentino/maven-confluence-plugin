package com.github.danielflower.mavenplugins.gitlog.filters;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Filters out commits that are simply the result of merging two branches.
 */
public class MergeCommitFilter implements CommitFilter {
	@Override
	public boolean renderCommit(RevCommit commit) {
		// A merge has two parents. Non-merge commits have a single parent,
		// or no parents for the first commit in the repository.
		return commit.getParentCount() < 2;
	}
}
