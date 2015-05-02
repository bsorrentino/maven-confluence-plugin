package com.github.danielflower.mavenplugins.gitlog.filters;

import org.eclipse.jgit.revwalk.RevCommit;

/**
 * Removes messages added by the Maven release plugin as those just tend to add noise.
 * Note that the tag name generated is still displayed.
 */
public class MavenReleasePluginMessageFilter implements CommitFilter {
	@Override
	public boolean renderCommit(RevCommit commit) {
		boolean isMavenRelease = commit.getShortMessage().startsWith("[maven-release-plugin]");
		return !isMavenRelease;
	}
}
