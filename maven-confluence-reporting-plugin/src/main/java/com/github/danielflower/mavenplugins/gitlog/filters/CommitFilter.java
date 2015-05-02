package com.github.danielflower.mavenplugins.gitlog.filters;

import org.eclipse.jgit.revwalk.RevCommit;

public interface CommitFilter {

	/**
	 * Returns true if the commit should be rendered; otherwise false.
	 * @param commit The commit to render
	 */
	boolean renderCommit(RevCommit commit);

}
