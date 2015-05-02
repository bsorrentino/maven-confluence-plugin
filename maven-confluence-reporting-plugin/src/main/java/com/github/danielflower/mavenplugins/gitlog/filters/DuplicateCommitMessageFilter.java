package com.github.danielflower.mavenplugins.gitlog.filters;

import org.eclipse.jgit.revwalk.RevCommit;

public class DuplicateCommitMessageFilter implements CommitFilter {

	private RevCommit previous;

	@Override
	public boolean renderCommit(RevCommit commit) {
		boolean isDuplicate = previous != null
				&& messagesEquivalent(commit.getShortMessage(), previous.getShortMessage());
		previous = commit;
		return !isDuplicate;
	}

	private boolean messagesEquivalent(String message1, String message2) {
		message1 = "" + message1.trim().toLowerCase();
		message2 = "" + message2.trim().toLowerCase();
		return message1.equals(message2);
	}
}
