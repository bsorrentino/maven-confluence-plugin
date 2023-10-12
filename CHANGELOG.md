# Maven Confluence Plugin - Changelog

<!-- Changelog for bsorrentino maven-confluence-plugin. -->

## Next release
### Generic changes

**Merge branch 'bugfix/curly_braces_in_table_#295' into develop**


[761397a935fbc5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/761397a935fbc5b) bsorrentino *2023-10-12 17:19:03*

**feat: enhance escapeMarkdownText method**

 * enable fine grained escaping on markdown text
 * resolve #295

[1157223261d86b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/1157223261d86b6) bsorrentino *2023-10-12 17:17:40*

**fix: enable escaping on TableCell**

 * working on #295

[ee97f605962a828](https://github.com/bsorrentino/maven-confluence-plugin/commit/ee97f605962a828) bsorrentino *2023-10-12 17:16:46*

**test: add unit test**

 * working on #295

[a65b74a001e9bb5](https://github.com/bsorrentino/maven-confluence-plugin/commit/a65b74a001e9bb5) bsorrentino *2023-10-12 17:16:07*

**Merge branch 'bugfix/relative_link_#294' into develop**


[3019d28e99fceff](https://github.com/bsorrentino/maven-confluence-plugin/commit/3019d28e99fceff) bsorrentino *2023-10-11 16:49:16*

**fix: check prefix before apply**

 * check link prefix before apply it
 * resolve #294

[c8d619ed504b8f7](https://github.com/bsorrentino/maven-confluence-plugin/commit/c8d619ed504b8f7) bsorrentino *2023-10-11 14:56:15*

**test: add test data**

 * prepare for reproduce issue
 * working #294

[c6550ce83a6b3d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6550ce83a6b3d7) bsorrentino *2023-10-11 14:55:23*

**build: move to next development version**


[e102f99740d02ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/e102f99740d02ab) bsorrentino *2023-10-11 08:29:13*

**refactor: remove lombok usage**


[5786d8cff33c956](https://github.com/bsorrentino/maven-confluence-plugin/commit/5786d8cff33c956) bsorrentino *2023-10-11 08:02:47*

**build: move to maven 3.6.0 compatibility**


[14ccb9769202ef8](https://github.com/bsorrentino/maven-confluence-plugin/commit/14ccb9769202ef8) bsorrentino *2023-10-11 07:56:27*

**Merge branch 'master' into develop**


[1158634c0129498](https://github.com/bsorrentino/maven-confluence-plugin/commit/1158634c0129498) bsorrentino *2023-06-26 09:07:59*

**Merge branch 'hotfix/changelog'**


[e3810ee009337d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/e3810ee009337d4) bsorrentino *2023-06-26 09:07:57*

**docs(changelog.md): update**


[e69cb1d0ab22f21](https://github.com/bsorrentino/maven-confluence-plugin/commit/e69cb1d0ab22f21) bsorrentino *2023-06-26 09:07:31*

**Merge tag 'v7.12' into develop**

 * new release

[8736e5277f5d031](https://github.com/bsorrentino/maven-confluence-plugin/commit/8736e5277f5d031) bsorrentino *2023-06-26 09:05:20*


## v7.12
### Generic changes

**Merge branch 'release/7.12'**


[a00f97c65531b21](https://github.com/bsorrentino/maven-confluence-plugin/commit/a00f97c65531b21) bsorrentino *2023-06-26 09:05:12*

**docs(readme.md): update**


[c6192c5efe8d032](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6192c5efe8d032) bsorrentino *2023-06-23 18:49:26*

**docs(changeme.md): update**


[a118da790dd92fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/a118da790dd92fb) bsorrentino *2023-06-23 18:40:04*

**build: move to next version 7.12**


[f70f9d5c308c422](https://github.com/bsorrentino/maven-confluence-plugin/commit/f70f9d5c308c422) bsorrentino *2023-06-23 18:38:02*

**build: move to next dev version**


[96d0999f4057f0d](https://github.com/bsorrentino/maven-confluence-plugin/commit/96d0999f4057f0d) bsorrentino *2023-06-23 18:17:05*

**Merge branch 'feature/pr288' into develop**


[c4b960e134429a2](https://github.com/bsorrentino/maven-confluence-plugin/commit/c4b960e134429a2) bsorrentino *2023-06-23 18:15:11*

**refactor(SiteProcessor.java): Optional usage refinement**

 * use Optional.OfNullable() instead Optional.of()

[220426a4c952a9f](https://github.com/bsorrentino/maven-confluence-plugin/commit/220426a4c952a9f) bsorrentino *2023-06-23 18:14:03*

**Merge branch 'williamschey-master' into feature/pr288**


[83e977cc998064b](https://github.com/bsorrentino/maven-confluence-plugin/commit/83e977cc998064b) bsorrentino *2023-06-23 18:07:45*

**#287: Changed to use Optional.ofNullable to deal with nulls**


[5997ce7ddffd33a](https://github.com/bsorrentino/maven-confluence-plugin/commit/5997ce7ddffd33a) William Schey *2023-06-23 08:06:10*

**#287: Fix for NPE that is throw when trying to link internally to project**


[622ae475a55a570](https://github.com/bsorrentino/maven-confluence-plugin/commit/622ae475a55a570) William Schey *2023-06-23 06:32:50*

**Merge branch 'master' into develop**


[90b6784c533e68f](https://github.com/bsorrentino/maven-confluence-plugin/commit/90b6784c533e68f) Build Pipeline *2023-03-23 09:05:22*

**Merge branch 'hotfix/changelog'**


[3ba40fc5a0c85de](https://github.com/bsorrentino/maven-confluence-plugin/commit/3ba40fc5a0c85de) Build Pipeline *2023-03-23 09:05:20*

**docs(CHANGELOG.md): update**


[345993d38a96642](https://github.com/bsorrentino/maven-confluence-plugin/commit/345993d38a96642) Build Pipeline *2023-03-23 09:05:00*

**Merge tag 'v7.11' into develop**

 * new release

[a3511124ab4627f](https://github.com/bsorrentino/maven-confluence-plugin/commit/a3511124ab4627f) Build Pipeline *2023-03-23 09:02:31*


## v7.11
### Generic changes

**Merge branch 'release/7.11'**


[c537baae22c86ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/c537baae22c86ed) Build Pipeline *2023-03-23 09:02:22*

**docs(README.md): update**


[e2bf2642b267fc3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e2bf2642b267fc3) Build Pipeline *2023-03-23 09:01:53*

**build: move to new production version**


[433a77c09a5acc0](https://github.com/bsorrentino/maven-confluence-plugin/commit/433a77c09a5acc0) Build Pipeline *2023-03-23 08:58:14*

**Merge branch 'bugfix/issue285' into develop**


[e9665cea785db3d](https://github.com/bsorrentino/maven-confluence-plugin/commit/e9665cea785db3d) Build Pipeline *2023-03-21 11:58:16*

**fix: finalize implementation**

 * implement &#x27;processConfluenceMacro&#x27; method in CommonmarkConfluenceWikiVisitor invoked on visit( HtmlBlock )
 * #285

[d5703c3836874e0](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5703c3836874e0) Build Pipeline *2023-03-21 11:57:53*

**test: improve test cases**

 * #285

[7e4ddefd59e0f80](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e4ddefd59e0f80) Build Pipeline *2023-03-21 11:56:11*

**feat: enable nested markdown processing**

 * #285

[0ff0fe8d7c75840](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ff0fe8d7c75840) Build Pipeline *2023-03-20 18:47:45*

**test: refactor test**

 * #285

[04f6530c2e56715](https://github.com/bsorrentino/maven-confluence-plugin/commit/04f6530c2e56715) Build Pipeline *2023-03-20 18:47:02*

**test(issue285.md): update publish test**

 * #285

[d657e4447e7542f](https://github.com/bsorrentino/maven-confluence-plugin/commit/d657e4447e7542f) Build Pipeline *2023-03-20 14:55:33*

**Merge branch 'bugfix/issue285' into develop**


[bf031092d0e16e0](https://github.com/bsorrentino/maven-confluence-plugin/commit/bf031092d0e16e0) bsorrentino *2023-03-20 09:40:26*

**fix(CommonmarkConfluenceWikiVisitor.java): update regex**

 * update regex for detecting html comment to handle multiline content
 * #285

[b3d6a756bc2089c](https://github.com/bsorrentino/maven-confluence-plugin/commit/b3d6a756bc2089c) bsorrentino *2023-03-20 09:40:02*

**test: test parse multiline html comment**

 * test multiline html comment for identify confluence macro + test publishing
 * #285

[17ba7f4988e367a](https://github.com/bsorrentino/maven-confluence-plugin/commit/17ba7f4988e367a) bsorrentino *2023-03-20 09:38:07*

**test(Issue285Test.kt): test parse multiline html comment**


[282538730cf0e33](https://github.com/bsorrentino/maven-confluence-plugin/commit/282538730cf0e33) bsorrentino *2023-03-19 18:58:45*

**refactor: remove the Optionals as arguments**


[b755fdf659e7044](https://github.com/bsorrentino/maven-confluence-plugin/commit/b755fdf659e7044) bsorrentino *2023-03-04 11:26:09*

**build(pom): move to next dev version**


[42b16e8ec0f8323](https://github.com/bsorrentino/maven-confluence-plugin/commit/42b16e8ec0f8323) bsorrentino *2023-03-04 11:24:28*

**Merge branch 'master' into develop**


[2e7485308678d97](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e7485308678d97) bsorrentino *2023-03-04 11:15:31*

**Merge branch 'hotfix/changelog'**


[d6f4ef7a3cea055](https://github.com/bsorrentino/maven-confluence-plugin/commit/d6f4ef7a3cea055) bsorrentino *2023-03-04 11:15:29*

**docs(changelog): update**


[2e120d732d1cbf1](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e120d732d1cbf1) bsorrentino *2023-03-04 11:15:11*

**Merge tag 'v7.10' into develop**

 * new release

[20e7233fad07815](https://github.com/bsorrentino/maven-confluence-plugin/commit/20e7233fad07815) bsorrentino *2023-03-04 11:13:15*


## v7.10
### Generic changes

**Merge branch 'release/7.10'**


[49eadcc08c06ce8](https://github.com/bsorrentino/maven-confluence-plugin/commit/49eadcc08c06ce8) bsorrentino *2023-03-04 11:13:05*

**docs(readme): update readme**


[1acb9afac862b02](https://github.com/bsorrentino/maven-confluence-plugin/commit/1acb9afac862b02) bsorrentino *2023-03-04 11:12:54*

**build(pom): move to next prod release**


[497d6e8c31e0e79](https://github.com/bsorrentino/maven-confluence-plugin/commit/497d6e8c31e0e79) bsorrentino *2023-03-04 11:09:45*

**docs(ConfluenceDeployMojo): update javadoc of MarkdownProcessorInfo parameter**

 * #284

[e4f6afdc05f1a70](https://github.com/bsorrentino/maven-confluence-plugin/commit/e4f6afdc05f1a70) Build Pipeline *2023-03-01 14:07:49*

**Merge branch 'feature/issue284' into develop**


[3af219cc5c7e0c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/3af219cc5c7e0c1) Build Pipeline *2023-03-01 12:32:44*

**feat: add support for <skipHtml> paramenter in <markdownProcessor> configuration**

 * #284

[39392f4a0e4b1fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/39392f4a0e4b1fe) Build Pipeline *2023-03-01 12:32:25*

**refactor(test-plugin/pom.xml): fix typo**


[332658cd0e8a6a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/332658cd0e8a6a8) Build Pipeline *2023-03-01 11:08:55*

**test: verify issue**

 * #284

[05f7dfcf6891e9b](https://github.com/bsorrentino/maven-confluence-plugin/commit/05f7dfcf6891e9b) Build Pipeline *2023-03-01 11:06:34*

**build: move to next developer version**


[f835f955cdc8384](https://github.com/bsorrentino/maven-confluence-plugin/commit/f835f955cdc8384) Build Pipeline *2023-03-01 11:04:49*

**docs: update release history**


[90a98c3c0ad284b](https://github.com/bsorrentino/maven-confluence-plugin/commit/90a98c3c0ad284b) bsorrentino *2023-01-05 09:39:51*

**Merge branch 'master' into develop**


[b98c86cd94b59dd](https://github.com/bsorrentino/maven-confluence-plugin/commit/b98c86cd94b59dd) Build Pipeline *2023-01-04 17:11:45*

**docs: update readme and changelog**


[6ed175521353c9b](https://github.com/bsorrentino/maven-confluence-plugin/commit/6ed175521353c9b) Build Pipeline *2023-01-04 17:11:26*

**Merge tag 'v7.9' into develop**

 * new release

[474a5ba9d07bc33](https://github.com/bsorrentino/maven-confluence-plugin/commit/474a5ba9d07bc33) Build Pipeline *2023-01-04 17:03:44*

**build: move to next developer version**


[0755ff99058ef79](https://github.com/bsorrentino/maven-confluence-plugin/commit/0755ff99058ef79) Build Pipeline *2022-12-28 16:02:39*

**Merge branch 'bugfix/issue282' into develop**


[7d1814877053e21](https://github.com/bsorrentino/maven-confluence-plugin/commit/7d1814877053e21) Build Pipeline *2022-12-28 15:56:39*

**test: markdown file to test the fix**

 * issue #282

[3d0af7544be5538](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d0af7544be5538) Build Pipeline *2022-12-28 15:56:20*

**fix(CommonmarkConfluenceWikiVisitor): avoid implicit use of String.format() in pre() and post() mothods of ChildrenProcessor class**

 * issue #282

[6c6a3b678221f79](https://github.com/bsorrentino/maven-confluence-plugin/commit/6c6a3b678221f79) Build Pipeline *2022-12-28 15:55:28*

**build(pom.xml): upgrade commonmark version**

 * upgrade commonmark version to 0.21.0
 * issue #282

[cb26a690c77dcac](https://github.com/bsorrentino/maven-confluence-plugin/commit/cb26a690c77dcac) Build Pipeline *2022-12-28 15:51:32*

**test(commonmark): add test to verify issue 282**

 * issue #282

[26b2025ca03eaef](https://github.com/bsorrentino/maven-confluence-plugin/commit/26b2025ca03eaef) Build Pipeline *2022-12-27 17:22:04*


## v7.9
### Generic changes

**Merge branch 'release/7.9'**


[b806b3be533c307](https://github.com/bsorrentino/maven-confluence-plugin/commit/b806b3be533c307) Build Pipeline *2023-01-04 17:03:35*

**chore: prepare for release 7.9**


[e08c9280fcf2d6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/e08c9280fcf2d6b) Build Pipeline *2023-01-04 17:03:20*

**docs(changelog): update changelog**


[10013a4db1c87f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/10013a4db1c87f5) Build Pipeline *2022-12-09 12:07:00*

**Merge tag 'v7.8' into develop**

 * release 7.8

[bb382a136fa4ef8](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb382a136fa4ef8) Build Pipeline *2022-12-09 12:04:56*

**docs(changelog): update changelog**


[74c2b9f99b3e27d](https://github.com/bsorrentino/maven-confluence-plugin/commit/74c2b9f99b3e27d) Build Pipeline *2022-12-09 11:38:59*

**Merge branch 'feature/pr281' into develop**


[1abca3dbe6afae6](https://github.com/bsorrentino/maven-confluence-plugin/commit/1abca3dbe6afae6) Build Pipeline *2022-12-09 11:36:10*

**refactor(ConfluenceService.Credentials): overload constructor to call the main one**

 * pr #281

[88842c23c756fe7](https://github.com/bsorrentino/maven-confluence-plugin/commit/88842c23c756fe7) Build Pipeline *2022-12-09 09:04:22*

**test: update credentials invocation**

 * PR #281

[1cbdf716cd71094](https://github.com/bsorrentino/maven-confluence-plugin/commit/1cbdf716cd71094) Build Pipeline *2022-12-07 17:58:38*

**refactor(ConflienceService): overload credentials constructor**

 * PR #281

[615eed43fc9f4e7](https://github.com/bsorrentino/maven-confluence-plugin/commit/615eed43fc9f4e7) Build Pipeline *2022-12-07 17:57:49*

**build: move to next development version**


[1714f231ba8fb82](https://github.com/bsorrentino/maven-confluence-plugin/commit/1714f231ba8fb82) Build Pipeline *2022-12-07 14:42:49*

**#280 fixed indent**


[6bdcdcb7843de54](https://github.com/bsorrentino/maven-confluence-plugin/commit/6bdcdcb7843de54) Dirk Mahler *2022-12-06 11:02:34*

**#280 Allow token based authentication using customer HTTP headers (e.g. Bearer scheme)**


[f87a94402062b71](https://github.com/bsorrentino/maven-confluence-plugin/commit/f87a94402062b71) Dirk Mahler *2022-12-06 10:49:26*

**Merge tag 'v7.7.1' into develop**

 * new minor version

[225a47cd72b1a9d](https://github.com/bsorrentino/maven-confluence-plugin/commit/225a47cd72b1a9d) bsorrentino *2022-11-16 19:43:00*

**Merge branch 'master' into develop**


[368a150f5e45e9a](https://github.com/bsorrentino/maven-confluence-plugin/commit/368a150f5e45e9a) bsorrentino *2022-11-16 19:12:42*

**Merge branch 'bugfix/pr279' into develop**


[13904b6163dbc51](https://github.com/bsorrentino/maven-confluence-plugin/commit/13904b6163dbc51) bsorrentino *2022-11-16 19:10:57*

**Merge remote-tracking branch 'origin/dependabot/maven/core/com.fasterxml.jackson.core-jackson-databind-2.12.7.1' into bugfix/pr279**


[b5ee8537b0bcafe](https://github.com/bsorrentino/maven-confluence-plugin/commit/b5ee8537b0bcafe) bsorrentino *2022-11-16 19:10:17*

**Merge branch 'master' into develop**


[2b63e994334aeff](https://github.com/bsorrentino/maven-confluence-plugin/commit/2b63e994334aeff) bsorrentino *2022-07-03 09:38:49*

**Merge tag 'v7.7' into develop**

 * new release

[bdb9dd2141340ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/bdb9dd2141340ab) bsorrentino *2022-07-03 09:36:58*

**Merge branch 'feature/pr266' into develop**


[4e0d10e1b60035c](https://github.com/bsorrentino/maven-confluence-plugin/commit/4e0d10e1b60035c) bsorrentino *2022-07-03 09:26:32*

**docs: update documentation**

 * #266

[ee07c6456cc2a7a](https://github.com/bsorrentino/maven-confluence-plugin/commit/ee07c6456cc2a7a) bsorrentino *2022-07-03 09:26:04*

**Merge branch 'jksevend-master' into feature/pr266**


[c667787c446b76a](https://github.com/bsorrentino/maven-confluence-plugin/commit/c667787c446b76a) bsorrentino *2022-07-03 08:27:24*

**Merge branch 'master' of https://github.com/jksevend/maven-confluence-plugin into jksevend-master**


[8cbfd066cee4142](https://github.com/bsorrentino/maven-confluence-plugin/commit/8cbfd066cee4142) bsorrentino *2022-07-03 08:26:07*

**refactor: log verbosity refinement**


[13d514b15b621f9](https://github.com/bsorrentino/maven-confluence-plugin/commit/13d514b15b621f9) bsorrentino *2022-07-03 08:24:58*

**Merge remote-tracking branch 'origin/dependabot/maven/deprecated/test-integration/src/test/resources/simple-plugin-project/org.codehaus.plexus-plexus-utils-3.0.16' into feature/pr266**


[f58757339616dde](https://github.com/bsorrentino/maven-confluence-plugin/commit/f58757339616dde) bsorrentino *2022-07-03 08:20:56*

**Merge remote-tracking branch 'origin/dependabot/maven/deprecated/test-integration/src/test/resources/plugin-project-goals-in-subpage/org.codehaus.plexus-plexus-utils-3.0.16' into feature/pr266**


[d3440a2b3293ddc](https://github.com/bsorrentino/maven-confluence-plugin/commit/d3440a2b3293ddc) bsorrentino *2022-07-03 08:20:18*

**Merge remote-tracking branch 'origin/dependabot/maven/org.codehaus.plexus-plexus-utils-3.0.16' into feature/pr266**


[5e277506efa7059](https://github.com/bsorrentino/maven-confluence-plugin/commit/5e277506efa7059) bsorrentino *2022-07-03 08:19:43*

**Merge remote-tracking branch 'origin/dependabot/maven/org.jetbrains.kotlin-kotlin-stdlib-1.6.0' into feature/pr266**


[613d112a7675252](https://github.com/bsorrentino/maven-confluence-plugin/commit/613d112a7675252) bsorrentino *2022-07-03 08:18:58*

**build: move to next developer version**


[bd53b67a472273e](https://github.com/bsorrentino/maven-confluence-plugin/commit/bd53b67a472273e) bsorrentino *2022-07-03 08:18:53*

**Bump plexus-utils**

 * Bumps [plexus-utils](https://github.com/sonatype/plexus-utils) from 3.0.8 to 3.0.16.
 * - [Release notes](https://github.com/sonatype/plexus-utils/releases)
 * - [Commits](https://github.com/sonatype/plexus-utils/compare/plexus-utils-3.0.8...plexus-utils-3.0.16)
 * ---
 * updated-dependencies:
 * - dependency-name: org.codehaus.plexus:plexus-utils
 * dependency-type: direct:production
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[bd2984fc34e2937](https://github.com/bsorrentino/maven-confluence-plugin/commit/bd2984fc34e2937) dependabot[bot] *2022-07-01 22:19:12*

**Bump plexus-utils**

 * Bumps [plexus-utils](https://github.com/sonatype/plexus-utils) from 3.0.8 to 3.0.16.
 * - [Release notes](https://github.com/sonatype/plexus-utils/releases)
 * - [Commits](https://github.com/sonatype/plexus-utils/compare/plexus-utils-3.0.8...plexus-utils-3.0.16)
 * ---
 * updated-dependencies:
 * - dependency-name: org.codehaus.plexus:plexus-utils
 * dependency-type: direct:production
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[32f038b80bf006c](https://github.com/bsorrentino/maven-confluence-plugin/commit/32f038b80bf006c) dependabot[bot] *2022-07-01 22:19:01*

**Bump plexus-utils from 3.0.8 to 3.0.16**

 * Bumps [plexus-utils](https://github.com/sonatype/plexus-utils) from 3.0.8 to 3.0.16.
 * - [Release notes](https://github.com/sonatype/plexus-utils/releases)
 * - [Commits](https://github.com/sonatype/plexus-utils/compare/plexus-utils-3.0.8...plexus-utils-3.0.16)
 * ---
 * updated-dependencies:
 * - dependency-name: org.codehaus.plexus:plexus-utils
 * dependency-type: direct:production
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[c5bc6af344fd891](https://github.com/bsorrentino/maven-confluence-plugin/commit/c5bc6af344fd891) dependabot[bot] *2022-07-01 22:17:33*

**Bump kotlin-stdlib from 1.5.20 to 1.6.0**

 * Bumps [kotlin-stdlib](https://github.com/JetBrains/kotlin) from 1.5.20 to 1.6.0.
 * - [Release notes](https://github.com/JetBrains/kotlin/releases)
 * - [Changelog](https://github.com/JetBrains/kotlin/blob/v1.6.0/ChangeLog.md)
 * - [Commits](https://github.com/JetBrains/kotlin/compare/v1.5.20...v1.6.0)
 * ---
 * updated-dependencies:
 * - dependency-name: org.jetbrains.kotlin:kotlin-stdlib
 * dependency-type: direct:development
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[f4407d6363f4172](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4407d6363f4172) dependabot[bot] *2022-06-20 22:47:41*

**Adding JSON Support**

 * - Tested

[c1201ad21afbf54](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1201ad21afbf54) Julian *2022-05-30 18:30:18*


## v7.8
### Generic changes

**Merge branch 'release/7.8'**


[5a323c9c160b9f2](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a323c9c160b9f2) Build Pipeline *2022-12-09 12:04:43*

**build: update version to next release**


[548da3f0e09bd90](https://github.com/bsorrentino/maven-confluence-plugin/commit/548da3f0e09bd90) Build Pipeline *2022-12-09 12:04:38*

**docs: update documentation**


[b2c477701450e30](https://github.com/bsorrentino/maven-confluence-plugin/commit/b2c477701450e30) Build Pipeline *2022-12-09 12:02:52*


## v7.7.1
### Generic changes

**Merge branch 'hotfix/7.7.1'**


[e2aad8fa16600af](https://github.com/bsorrentino/maven-confluence-plugin/commit/e2aad8fa16600af) bsorrentino *2022-11-16 19:42:47*

**build: update version**


[d29f009e0948e80](https://github.com/bsorrentino/maven-confluence-plugin/commit/d29f009e0948e80) bsorrentino *2022-11-16 19:41:45*

**fix: update jackson.version**


[7793bfd5a591b0a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7793bfd5a591b0a) bsorrentino *2022-11-16 19:41:12*

**chore: update ignore**


[5889ff3c17e160e](https://github.com/bsorrentino/maven-confluence-plugin/commit/5889ff3c17e160e) bsorrentino *2022-11-16 19:39:15*

**Merge branch 'hotfix/pr279'**


[e5006a61bbccda5](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5006a61bbccda5) bsorrentino *2022-11-16 19:12:22*

**build(deps): bump jackson-databind from 2.12.7 to 2.12.7.1 in /core**

 * Bumps [jackson-databind](https://github.com/FasterXML/jackson) from 2.12.7 to 2.12.7.1.
 * - [Release notes](https://github.com/FasterXML/jackson/releases)
 * - [Commits](https://github.com/FasterXML/jackson/commits)
 * ---
 * updated-dependencies:
 * - dependency-name: com.fasterxml.jackson.core:jackson-databind
 * dependency-type: direct:production
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[faacebdbfa0e682](https://github.com/bsorrentino/maven-confluence-plugin/commit/faacebdbfa0e682) dependabot[bot] *2022-11-16 03:17:00*

**Merge branch 'hotfix/changelog'**


[54725c0add5d5e4](https://github.com/bsorrentino/maven-confluence-plugin/commit/54725c0add5d5e4) bsorrentino *2022-07-03 09:38:46*

**docs: update changelog**


[a0b7d62758f5c64](https://github.com/bsorrentino/maven-confluence-plugin/commit/a0b7d62758f5c64) bsorrentino *2022-07-03 09:38:32*


## v7.7
### Generic changes

**Merge branch 'release/7.7'**


[d9ebdd04542ff85](https://github.com/bsorrentino/maven-confluence-plugin/commit/d9ebdd04542ff85) bsorrentino *2022-07-03 09:36:47*

**build: move to next release**


[e592d4b897f1557](https://github.com/bsorrentino/maven-confluence-plugin/commit/e592d4b897f1557) bsorrentino *2022-07-03 09:36:32*

**docs: update docs**


[9663dc4f774d0eb](https://github.com/bsorrentino/maven-confluence-plugin/commit/9663dc4f774d0eb) bsorrentino *2022-07-03 09:33:37*

**docs: update docs**


[c3880713797ccb8](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3880713797ccb8) bsorrentino *2022-07-03 09:30:00*

**docs: update changelog**


[e47e6c58cc4b02c](https://github.com/bsorrentino/maven-confluence-plugin/commit/e47e6c58cc4b02c) bsorrentino *2022-06-03 15:32:11*

**Merge tag 'v7.6' into develop**

 * new release

[61d1bdaece58241](https://github.com/bsorrentino/maven-confluence-plugin/commit/61d1bdaece58241) bsorrentino *2022-06-03 15:30:11*

**Merge branch 'feature/pr267' into develop**


[a9413fb9d78e4be](https://github.com/bsorrentino/maven-confluence-plugin/commit/a9413fb9d78e4be) bsorrentino *2022-06-03 13:21:44*

**Merge branch 'feature/multipleJiraInstances' of https://github.com/tspindler/maven-confluence-plugin into feature/pr267**


[dfbbd1c66d45fba](https://github.com/bsorrentino/maven-confluence-plugin/commit/dfbbd1c66d45fba) bsorrentino *2022-06-03 13:20:46*

**Merge branch 'feature/pr265' into develop**


[f87fe0a07c9ffaa](https://github.com/bsorrentino/maven-confluence-plugin/commit/f87fe0a07c9ffaa) bsorrentino *2022-06-03 13:15:39*

**fix(core): upgrade jackson from 2.12.0 to version 2.12.7 to fix security issue**

 * fix PR #265

[87210cc8ad24d50](https://github.com/bsorrentino/maven-confluence-plugin/commit/87210cc8ad24d50) bsorrentino *2022-06-03 13:15:21*

**Merge remote-tracking branch 'origin/dependabot/maven/core/com.fasterxml.jackson.core-jackson-databind-2.12.6.1' into feature/pr265**


[562a79aaf2963da](https://github.com/bsorrentino/maven-confluence-plugin/commit/562a79aaf2963da) bsorrentino *2022-06-03 13:05:20*

**build: move to next development version**


[2977d45b00956b7](https://github.com/bsorrentino/maven-confluence-plugin/commit/2977d45b00956b7) bsorrentino *2022-06-03 13:03:34*

**added function to define jira instance baseurl bsorrentino#136**


[ecea4d2b9767cc5](https://github.com/bsorrentino/maven-confluence-plugin/commit/ecea4d2b9767cc5) tspindler *2022-06-01 12:13:07*

**Merge tag 'v7.5' into develop**

 * new release

[a37e81df195468e](https://github.com/bsorrentino/maven-confluence-plugin/commit/a37e81df195468e) bsorrentino *2022-04-01 11:24:23*

**Bump jackson-databind from 2.12.0 to 2.12.6.1 in /core**

 * Bumps [jackson-databind](https://github.com/FasterXML/jackson) from 2.12.0 to 2.12.6.1.
 * - [Release notes](https://github.com/FasterXML/jackson/releases)
 * - [Commits](https://github.com/FasterXML/jackson/commits)
 * ---
 * updated-dependencies:
 * - dependency-name: com.fasterxml.jackson.core:jackson-databind
 * dependency-type: direct:production
 * ...
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[7df5e821ee91132](https://github.com/bsorrentino/maven-confluence-plugin/commit/7df5e821ee91132) dependabot[bot] *2022-04-01 11:06:28*


## v7.6
### Generic changes

**Merge branch 'release/7.6'**


[bb7dc9275a7a027](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb7dc9275a7a027) bsorrentino *2022-06-03 15:30:02*

**docs: update readme**


[98d2acadcba21c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/98d2acadcba21c1) bsorrentino *2022-06-03 15:29:26*

**docs: update changelog**


[c94c833d427774b](https://github.com/bsorrentino/maven-confluence-plugin/commit/c94c833d427774b) bsorrentino *2022-06-03 15:29:09*

**build: set version to next release**


[f6d7c5895144f96](https://github.com/bsorrentino/maven-confluence-plugin/commit/f6d7c5895144f96) bsorrentino *2022-06-03 15:25:39*


## v7.5
### Generic changes

**Merge branch 'hotfix/7.5'**


[038ffb96acf802f](https://github.com/bsorrentino/maven-confluence-plugin/commit/038ffb96acf802f) bsorrentino *2022-04-01 11:24:15*

**update release version**


[f7ee12eaf4bda15](https://github.com/bsorrentino/maven-confluence-plugin/commit/f7ee12eaf4bda15) bsorrentino *2022-04-01 11:24:06*

**update changelog**


[6097399e98bb019](https://github.com/bsorrentino/maven-confluence-plugin/commit/6097399e98bb019) bsorrentino *2022-04-01 11:09:57*

**Merge branch 'master' into develop**


[417cfcd604ba0f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/417cfcd604ba0f5) bsorrentino *2022-04-01 11:05:41*

**Merge branch 'hotfix/docs'**


[72789ec60aac667](https://github.com/bsorrentino/maven-confluence-plugin/commit/72789ec60aac667) bsorrentino *2022-04-01 11:05:39*

**update readme**


[8526cd456884e12](https://github.com/bsorrentino/maven-confluence-plugin/commit/8526cd456884e12) bsorrentino *2022-04-01 11:05:23*

**Merge tag 'v7.5' into develop**

 * new release

[9828cee1b4e44fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/9828cee1b4e44fe) bsorrentino *2022-04-01 11:01:49*

**Merge branch 'release/7.5'**


[65d6021096656d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/65d6021096656d1) bsorrentino *2022-04-01 11:01:38*

**uodate changelog**


[79398e8547d73f4](https://github.com/bsorrentino/maven-confluence-plugin/commit/79398e8547d73f4) bsorrentino *2022-04-01 11:01:17*

**upgrade it-changelog-maven-plugin version to 1.89**


[cf465163396227d](https://github.com/bsorrentino/maven-confluence-plugin/commit/cf465163396227d) bsorrentino *2022-04-01 11:00:01*

**actions: enable 'on release' trigger**


[29df83d3451100e](https://github.com/bsorrentino/maven-confluence-plugin/commit/29df83d3451100e) bsorrentino *2022-04-01 10:44:18*

**javadoc plugin: change from <additionalparam> to <additionalJOptions>**


[24aae5125d5475e](https://github.com/bsorrentino/maven-confluence-plugin/commit/24aae5125d5475e) bsorrentino *2022-04-01 10:42:04*

**set next developer version**


[c1712d43107908b](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1712d43107908b) bsorrentino *2022-03-30 21:16:02*

**Merge branch 'bugfix/issue264' into develop**


[478ea0fb70203c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/478ea0fb70203c1) bsorrentino *2022-03-30 21:11:47*

**call save() after file creation at first DeployStateManager load invocation**


[c1e3f0583f0c7ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1e3f0583f0c7ac) bsorrentino *2022-03-30 21:11:13*

**upgrade kotlin version to 1.5.20**


[40aa9dfecaca64b](https://github.com/bsorrentino/maven-confluence-plugin/commit/40aa9dfecaca64b) bsorrentino *2022-03-30 21:08:40*

**fix import for unit test**


[d960a7ae58811db](https://github.com/bsorrentino/maven-confluence-plugin/commit/d960a7ae58811db) bsorrentino *2022-03-30 21:07:55*

**update pom for better support of multi jdk compilation**


[5464e7a3fea6a32](https://github.com/bsorrentino/maven-confluence-plugin/commit/5464e7a3fea6a32) bsorrentino *2022-03-30 21:07:29*

**upgrade commonmark version to 0.18.2**

 * update pom for better support of multi jdk compilation

[5d87db4c85f7ac8](https://github.com/bsorrentino/maven-confluence-plugin/commit/5d87db4c85f7ac8) bsorrentino *2022-03-30 21:06:16*

**update pom for better support of multi jdk compilation**


[fe8ad5515948722](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe8ad5515948722) bsorrentino *2022-03-30 21:05:38*

**update pom for better support of multi jdk compilation**


[2cfe9251703e49f](https://github.com/bsorrentino/maven-confluence-plugin/commit/2cfe9251703e49f) bsorrentino *2022-03-30 21:05:24*

**fix: when the tableCell value is empty a space is added. #264**


[5742b0627a942b1](https://github.com/bsorrentino/maven-confluence-plugin/commit/5742b0627a942b1) bsorrentino *2022-03-30 21:03:38*

**add unit test #264**


[6c812f953895871](https://github.com/bsorrentino/maven-confluence-plugin/commit/6c812f953895871) bsorrentino *2022-03-29 15:33:15*

**update test**


[4045e23249cf3db](https://github.com/bsorrentino/maven-confluence-plugin/commit/4045e23249cf3db) bsorrentino *2022-03-29 15:32:47*

**update docker compose**


[8c829b85b5da7f9](https://github.com/bsorrentino/maven-confluence-plugin/commit/8c829b85b5da7f9) bsorrentino *2022-03-11 14:39:55*

**update docker compose**


[1cd8018ddbee3b2](https://github.com/bsorrentino/maven-confluence-plugin/commit/1cd8018ddbee3b2) bsorrentino *2022-03-10 17:24:40*

**Merge branch 'bugfix/issue263' into develop**


[04d2664c9e500f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/04d2664c9e500f3) bsorrentino *2022-03-10 14:07:26*

**update docker stuff**


[aa24af1b9cd87c4](https://github.com/bsorrentino/maven-confluence-plugin/commit/aa24af1b9cd87c4) bsorrentino *2022-03-10 14:07:05*

**test scm plugin configuration**

 * #263

[60effa6b4b6746a](https://github.com/bsorrentino/maven-confluence-plugin/commit/60effa6b4b6746a) bsorrentino *2022-03-08 22:53:38*

**prepare plugin test**


[f9964afe169129f](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9964afe169129f) bsorrentino *2022-03-08 22:38:50*

**refactor docker stuff**


[9a323a0a2b144fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a323a0a2b144fe) bsorrentino *2022-03-08 22:38:28*

**update .gitignore**


[a14f3478db64964](https://github.com/bsorrentino/maven-confluence-plugin/commit/a14f3478db64964) bsorrentino *2022-03-08 22:37:35*

**update(test)**

 * refine test for confluence cloud

[7209e63e2d55c7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/7209e63e2d55c7e) bsorrentino *2022-01-17 17:38:46*

**update(test)**

 * add test for confluence cloud

[8f4b69f3d90a829](https://github.com/bsorrentino/maven-confluence-plugin/commit/8f4b69f3d90a829) bsorrentino *2022-01-17 16:32:50*

**update(docker)**

 * add confluence server 7.15

[526766d3e46783e](https://github.com/bsorrentino/maven-confluence-plugin/commit/526766d3e46783e) bsorrentino *2022-01-17 16:32:31*

**update(test)**

 * add emphasis.wiki test

[c59098cdd5a85eb](https://github.com/bsorrentino/maven-confluence-plugin/commit/c59098cdd5a85eb) bsorrentino *2022-01-17 13:59:35*

**Merge branch 'master' into develop**


[6a6ca86af5b1c67](https://github.com/bsorrentino/maven-confluence-plugin/commit/6a6ca86af5b1c67) bsorrentino *2022-01-16 16:38:48*

**Merge branch 'hotfix/doc'**


[9e37eefbb603fea](https://github.com/bsorrentino/maven-confluence-plugin/commit/9e37eefbb603fea) bsorrentino *2022-01-16 16:38:29*

**update(doc)**


[5d4880428a7da08](https://github.com/bsorrentino/maven-confluence-plugin/commit/5d4880428a7da08) bsorrentino *2022-01-16 16:38:15*

**Merge branch 'master' into develop**


[b9299da85df6b78](https://github.com/bsorrentino/maven-confluence-plugin/commit/b9299da85df6b78) bsorrentino *2022-01-16 16:22:42*

**Merge branch 'hotfix/deploy-pages'**


[e6b5bbe76a14c7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/e6b5bbe76a14c7b) bsorrentino *2022-01-16 16:20:05*

**update(action):**

 * update deploy-pages action to include javadoc

[fe6cdfd92e0558a](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe6cdfd92e0558a) bsorrentino *2022-01-16 16:19:38*

**update(pom):**

 * update javadoc plugin version
 * generate javadoc for core module on install phase

[2331544aa1bfe1c](https://github.com/bsorrentino/maven-confluence-plugin/commit/2331544aa1bfe1c) bsorrentino *2022-01-16 15:37:23*

**update(layout)**

 * add deprecated folder

[910c4d5be43da85](https://github.com/bsorrentino/maven-confluence-plugin/commit/910c4d5be43da85) bsorrentino *2022-01-16 12:23:48*

**Merge branch 'master' into develop**


[a7857709a2cda1c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a7857709a2cda1c) bsorrentino *2022-01-15 17:10:18*

**Merge branch 'hotfix/doc'**


[13dd70d3b6b5758](https://github.com/bsorrentino/maven-confluence-plugin/commit/13dd70d3b6b5758) bsorrentino *2022-01-15 17:10:12*

**update(doc)**


[7736b4dd2a5b90a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7736b4dd2a5b90a) bsorrentino *2022-01-15 17:09:59*

**test(childrenTitlesPrefixed)**


[8b0c57a5dfb44c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/8b0c57a5dfb44c5) bsorrentino *2022-01-15 16:36:34*

**update(version):**

 * Move to next development release

[1f778ac0c63a3b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/1f778ac0c63a3b6) Build Pipeline *2022-01-11 10:02:39*

**update(history)**


[8a4bd70be332069](https://github.com/bsorrentino/maven-confluence-plugin/commit/8a4bd70be332069) Build Pipeline *2022-01-11 10:01:14*

**Merge branch 'master' into develop**


[24d3ba022a08ed1](https://github.com/bsorrentino/maven-confluence-plugin/commit/24d3ba022a08ed1) Build Pipeline *2022-01-11 09:59:50*

**Merge branch 'hotfix/doc'**


[7b6dbdadbe0df24](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b6dbdadbe0df24) Build Pipeline *2022-01-11 09:59:47*

**update(doc)**


[359c41d3663412d](https://github.com/bsorrentino/maven-confluence-plugin/commit/359c41d3663412d) Build Pipeline *2022-01-11 09:59:34*

**Merge branch 'master' into develop**


[377a7ce7ea8d29c](https://github.com/bsorrentino/maven-confluence-plugin/commit/377a7ce7ea8d29c) Build Pipeline *2022-01-10 14:45:28*

**Merge branch 'hotfix/changelog'**


[e8d1ad232a39169](https://github.com/bsorrentino/maven-confluence-plugin/commit/e8d1ad232a39169) Build Pipeline *2022-01-10 14:45:26*

**update changelog**


[d1ee21cbe77620d](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1ee21cbe77620d) Build Pipeline *2022-01-10 14:45:18*

**Merge tag 'v7.4' into develop**

 * new release

[c6da6ca472d0d83](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6da6ca472d0d83) Build Pipeline *2022-01-10 14:43:46*

**Merge branch 'bugfix/issue261' into develop**


[46430538b21c801](https://github.com/bsorrentino/maven-confluence-plugin/commit/46430538b21c801) Build Pipeline *2022-01-10 14:36:30*

**issue #261**

 * update the &#x27;minitemplator&#x27; dep to last release

[ab3912ed9825ddc](https://github.com/bsorrentino/maven-confluence-plugin/commit/ab3912ed9825ddc) Build Pipeline *2022-01-10 14:32:44*

**update(actions)**

 * move to http to https

[2157bae87bc8528](https://github.com/bsorrentino/maven-confluence-plugin/commit/2157bae87bc8528) Build Pipeline *2022-01-10 14:29:04*

**issue #261**

 * update the &#x27;minitemplator&#x27; dep to last development release

[3646d7df8b1df8a](https://github.com/bsorrentino/maven-confluence-plugin/commit/3646d7df8b1df8a) Build Pipeline *2022-01-07 14:19:18*

**clean(code)**


[3855da9a3c4cb20](https://github.com/bsorrentino/maven-confluence-plugin/commit/3855da9a3c4cb20) bsorrentino *2021-10-13 15:14:29*

**clean(code): update publish test**


[d7b237b68c84e72](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7b237b68c84e72) bsorrentino *2021-10-13 15:04:05*

**clean(code): update publish test**


[5dcebfb443a5d1b](https://github.com/bsorrentino/maven-confluence-plugin/commit/5dcebfb443a5d1b) bsorrentino *2021-10-13 15:03:40*

**test(issue): add test for issue #261**


[85a4206c0b3d9ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/85a4206c0b3d9ff) bsorrentino *2021-10-13 14:16:36*

**task(build)**

 * upgrade git-changelog-maven-plugin version -&gt; 1.78

[4c7bcc47ec7dbfd](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c7bcc47ec7dbfd) bsorrentino *2021-10-12 10:53:14*

**Merge tag 'v7.3.2' into develop**

 * release that fix 7.3.1

[e005473f63819b7](https://github.com/bsorrentino/maven-confluence-plugin/commit/e005473f63819b7) bsorrentino *2021-08-09 11:24:21*

**Merge branch 'master' into develop**


[fc179dee33da18f](https://github.com/bsorrentino/maven-confluence-plugin/commit/fc179dee33da18f) bsorrentino *2021-08-09 09:04:35*

**clean code**


[1481415a6503d81](https://github.com/bsorrentino/maven-confluence-plugin/commit/1481415a6503d81) Build Pipeline *2021-08-07 10:28:16*

**move to next dev version**


[c0aef8b34fd9199](https://github.com/bsorrentino/maven-confluence-plugin/commit/c0aef8b34fd9199) Build Pipeline *2021-08-07 10:25:30*

**Merge tag 'v7.3.1' into develop**

 * minor release 7.3.1

[023018e74cccb69](https://github.com/bsorrentino/maven-confluence-plugin/commit/023018e74cccb69) Build Pipeline *2021-08-07 09:09:57*


## v7.4
### Generic changes

**Merge branch 'release/7.4'**


[35891a666918dc9](https://github.com/bsorrentino/maven-confluence-plugin/commit/35891a666918dc9) Build Pipeline *2022-01-10 14:43:38*

**move to next release**


[83f8aea3aa353ef](https://github.com/bsorrentino/maven-confluence-plugin/commit/83f8aea3aa353ef) Build Pipeline *2022-01-10 14:41:36*

**update changelog plugin version**


[814f3e14abd50b0](https://github.com/bsorrentino/maven-confluence-plugin/commit/814f3e14abd50b0) Build Pipeline *2022-01-10 14:39:52*


## v7.3.2
### Generic changes

**Merge branch 'hotfix/7.3.2'**


[75f221b01c37bc0](https://github.com/bsorrentino/maven-confluence-plugin/commit/75f221b01c37bc0) bsorrentino *2021-08-09 11:19:43*

**update documentation for new release**


[6bd342e3b376838](https://github.com/bsorrentino/maven-confluence-plugin/commit/6bd342e3b376838) bsorrentino *2021-08-09 11:19:12*

**move to next release**


[f4594441d700a3c](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4594441d700a3c) bsorrentino *2021-08-09 09:08:15*

**Merge branch 'hotfix/256'**


[ee1853902786134](https://github.com/bsorrentino/maven-confluence-plugin/commit/ee1853902786134) bsorrentino *2021-08-09 09:04:32*

**fix bug on solution provided in #256 - this solve also the PR #257**


[8fcdd5a758a0102](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fcdd5a758a0102) bsorrentino *2021-08-09 08:58:35*


## v7.3.1
### Generic changes

**Merge branch 'release/7.3.1'**


[117a7a20e9b9944](https://github.com/bsorrentino/maven-confluence-plugin/commit/117a7a20e9b9944) Build Pipeline *2021-08-07 09:09:43*

**update changelog**


[4882997c65bf742](https://github.com/bsorrentino/maven-confluence-plugin/commit/4882997c65bf742) Build Pipeline *2021-08-07 09:09:25*

**move to next release**


[09b3b9ade9a922f](https://github.com/bsorrentino/maven-confluence-plugin/commit/09b3b9ade9a922f) Build Pipeline *2021-08-07 09:07:55*

**update readme**


[1fc51b04eb3e898](https://github.com/bsorrentino/maven-confluence-plugin/commit/1fc51b04eb3e898) Build Pipeline *2021-08-07 09:05:44*

**move to next dev version**


[52a63a3ebfdea71](https://github.com/bsorrentino/maven-confluence-plugin/commit/52a63a3ebfdea71) bsorrentino *2021-08-06 10:08:31*

**issue #256 - translate timeout in mills for XLMRPC protocol**


[209e628e7deebdc](https://github.com/bsorrentino/maven-confluence-plugin/commit/209e628e7deebdc) bsorrentino *2021-08-06 10:05:26*

**Merge tag 'v7.3' into develop**

 * release 7.3

[1580084ce4e6fe7](https://github.com/bsorrentino/maven-confluence-plugin/commit/1580084ce4e6fe7) bsorrentino *2021-07-31 16:43:04*

**Merge branch 'bugfix/255' into develop**


[4ab33f25a04760c](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ab33f25a04760c) bsorrentino *2021-07-28 16:13:53*

**issue #255 - update unit test**


[aebec40014f3273](https://github.com/bsorrentino/maven-confluence-plugin/commit/aebec40014f3273) bsorrentino *2021-07-28 16:12:18*

**issue #255 - update for use of  commonmark to 0.17.0**


[7fd5e86fea10c0c](https://github.com/bsorrentino/maven-confluence-plugin/commit/7fd5e86fea10c0c) bsorrentino *2021-07-28 16:11:57*

**issue #255 - add newline after OrderList and BulletList only if it is not nested**


[5d69b6895f975c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/5d69b6895f975c5) bsorrentino *2021-07-28 16:10:21*

**issue #255**

 * upgrade commonmark to 0.17.0

[56a5091dd731b9a](https://github.com/bsorrentino/maven-confluence-plugin/commit/56a5091dd731b9a) bsorrentino *2021-07-28 16:05:51*

**Merge branch 'master' into develop**


[a85d94c3e0ca575](https://github.com/bsorrentino/maven-confluence-plugin/commit/a85d94c3e0ca575) bsorrentino *2021-07-26 18:35:21*

**move to next development version**


[96e83ee3341e04a](https://github.com/bsorrentino/maven-confluence-plugin/commit/96e83ee3341e04a) bsorrentino *2021-07-26 18:30:42*

**Merge tag 'v7.2.1' into develop**

 * new minor release

[59ae81b36f5090c](https://github.com/bsorrentino/maven-confluence-plugin/commit/59ae81b36f5090c) bsorrentino *2021-07-19 15:23:10*


## v7.3
### Generic changes

**Merge branch 'release/7.3'**


[82d62674cd10ad3](https://github.com/bsorrentino/maven-confluence-plugin/commit/82d62674cd10ad3) bsorrentino *2021-07-31 16:42:53*

**update markdown test**


[d02d2bdff71a07d](https://github.com/bsorrentino/maven-confluence-plugin/commit/d02d2bdff71a07d) bsorrentino *2021-07-31 16:42:29*

**update changelog**


[74fcdd76effc788](https://github.com/bsorrentino/maven-confluence-plugin/commit/74fcdd76effc788) bsorrentino *2021-07-31 16:33:55*

**upgrade javax.json package**


[9f39ce6e3cc8c38](https://github.com/bsorrentino/maven-confluence-plugin/commit/9f39ce6e3cc8c38) bsorrentino *2021-07-31 16:31:45*

**update readme**


[4f113319d4ce5d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/4f113319d4ce5d8) bsorrentino *2021-07-31 16:30:59*

**remove deprecation**


[b24a5c21c3c9304](https://github.com/bsorrentino/maven-confluence-plugin/commit/b24a5c21c3c9304) bsorrentino *2021-07-31 16:07:12*

**move to next release**


[17eb45e7cfdb103](https://github.com/bsorrentino/maven-confluence-plugin/commit/17eb45e7cfdb103) bsorrentino *2021-07-31 16:06:14*

**Merge branch 'hotfix/doc'**


[3d2576f2e3a6c77](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d2576f2e3a6c77) bsorrentino *2021-07-26 18:35:17*

**update readme**


[0284723f35e16fa](https://github.com/bsorrentino/maven-confluence-plugin/commit/0284723f35e16fa) bsorrentino *2021-07-26 18:35:07*


## v7.2.1
### Generic changes

**Merge branch 'release/7.2.1'**


[f4d6bd69bc6d293](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4d6bd69bc6d293) bsorrentino *2021-07-19 15:23:00*

**update changelog**


[a140268ed3a2e99](https://github.com/bsorrentino/maven-confluence-plugin/commit/a140268ed3a2e99) bsorrentino *2021-07-19 15:22:26*

**move to next release**


[b00f8b4fbe2d3f9](https://github.com/bsorrentino/maven-confluence-plugin/commit/b00f8b4fbe2d3f9) bsorrentino *2021-07-19 15:21:04*

**update readme**


[175ec5dc4bbe3c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/175ec5dc4bbe3c2) bsorrentino *2021-07-19 15:20:33*

**Merge branch 'bugfix/issue253' into develop**


[bbca44ffeea5f3d](https://github.com/bsorrentino/maven-confluence-plugin/commit/bbca44ffeea5f3d) bsorrentino *2021-07-19 15:15:46*

**update warnin message**


[834b27ab9e7e35a](https://github.com/bsorrentino/maven-confluence-plugin/commit/834b27ab9e7e35a) Build Pipeline *2021-07-15 14:48:45*

**issue #253 - catch NPE on dependencies collector**


[78c2eb29e51b5f1](https://github.com/bsorrentino/maven-confluence-plugin/commit/78c2eb29e51b5f1) Build Pipeline *2021-07-15 14:10:27*

**add ConfluenceService Proxy for debug purpose**


[b9e103b8b635aea](https://github.com/bsorrentino/maven-confluence-plugin/commit/b9e103b8b635aea) bsorrentino *2021-07-15 14:09:15*

**move to nexe development version**


[14445163ab44ba8](https://github.com/bsorrentino/maven-confluence-plugin/commit/14445163ab44ba8) bsorrentino *2021-07-15 14:09:15*

**add ConfluenceService Proxy for debug purpose**


[68421c7daa382bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/68421c7daa382bb) bsorrentino *2021-07-07 16:02:10*

**move to nexe development version**


[3276b6af1f5c8e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/3276b6af1f5c8e6) bsorrentino *2021-06-11 08:52:39*

**Merge tag 'v7.2' into develop**

 * new release

[13ae8aae0518da4](https://github.com/bsorrentino/maven-confluence-plugin/commit/13ae8aae0518da4) bsorrentino *2021-06-10 09:46:53*


## v7.2
### Generic changes

**Merge branch 'release/7.2'**


[430211a648fc6e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/430211a648fc6e6) bsorrentino *2021-06-10 09:46:42*

**update changelog**


[e9a1028a7f54970](https://github.com/bsorrentino/maven-confluence-plugin/commit/e9a1028a7f54970) bsorrentino *2021-06-10 09:44:14*

**update release**


[7108b3def51b243](https://github.com/bsorrentino/maven-confluence-plugin/commit/7108b3def51b243) bsorrentino *2021-06-10 09:22:43*

**update readme**


[492e4eea3bf0b42](https://github.com/bsorrentino/maven-confluence-plugin/commit/492e4eea3bf0b42) bsorrentino *2021-06-10 09:18:21*

**Merge branch 'feature/issue252' into develop**


[6b8565ffa2ba174](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b8565ffa2ba174) bsorrentino *2021-06-10 00:06:55*

**issue #252 - add version increment**


[cec540da163d883](https://github.com/bsorrentino/maven-confluence-plugin/commit/cec540da163d883) bsorrentino *2021-06-10 00:06:32*

**issue #252 - add support for version attribute**


[24e1a0fc631adad](https://github.com/bsorrentino/maven-confluence-plugin/commit/24e1a0fc631adad) bsorrentino *2021-06-09 17:19:01*

**move to next develop version**


[c302540359443ae](https://github.com/bsorrentino/maven-confluence-plugin/commit/c302540359443ae) bsorrentino *2021-06-09 13:01:20*

**Merge branch 'master' into develop**


[f9785fa162e46e1](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9785fa162e46e1) bsorrentino *2021-06-01 10:38:27*

**Merge branch 'hotfix/changelog'**


[941caac6c725f05](https://github.com/bsorrentino/maven-confluence-plugin/commit/941caac6c725f05) bsorrentino *2021-06-01 10:38:25*

**update changelog**


[e98ba924afd76d3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e98ba924afd76d3) bsorrentino *2021-06-01 10:38:13*

**Merge tag 'v7.1' into develop**

 * release 7.1

[20494871d45bd50](https://github.com/bsorrentino/maven-confluence-plugin/commit/20494871d45bd50) bsorrentino *2021-06-01 10:36:11*


## v7.1
### Generic changes

**Merge branch 'release/7.1'**


[cd2d8aa7b59362f](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd2d8aa7b59362f) bsorrentino *2021-06-01 10:36:02*

**update changelog**


[09e5da2975efa81](https://github.com/bsorrentino/maven-confluence-plugin/commit/09e5da2975efa81) bsorrentino *2021-06-01 10:35:34*

**update readme**


[8fef3a9d33cc21c](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fef3a9d33cc21c) bsorrentino *2021-06-01 10:34:38*

**move to new release version**


[f6173e4e1ae7d9c](https://github.com/bsorrentino/maven-confluence-plugin/commit/f6173e4e1ae7d9c) bsorrentino *2021-06-01 10:29:17*

**update minitemplator version 1.3**


[29a2faf6a503d61](https://github.com/bsorrentino/maven-confluence-plugin/commit/29a2faf6a503d61) bsorrentino *2021-06-01 10:24:53*

**update minitemplator version 1.3**


[85ba4801d1060f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/85ba4801d1060f5) bsorrentino *2021-06-01 10:24:38*

**issue #248 - remove exception on multiple markdown processor initialization**


[12b1c3d1111f8d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/12b1c3d1111f8d8) bsorrentino *2021-05-29 13:50:54*

**Merge tag 'v7.0' into develop**

 * release 7.0

[e79f0f22b220125](https://github.com/bsorrentino/maven-confluence-plugin/commit/e79f0f22b220125) bsorrentino *2021-05-18 10:05:02*


## v7.0
### Generic changes

**Merge branch 'release/7.0'**


[cb2f22ac41f54aa](https://github.com/bsorrentino/maven-confluence-plugin/commit/cb2f22ac41f54aa) bsorrentino *2021-05-18 10:04:52*

**prepare for release 7.0**


[cf34c7e85393d10](https://github.com/bsorrentino/maven-confluence-plugin/commit/cf34c7e85393d10) bsorrentino *2021-05-18 10:04:28*

**add space attribute to newPage factory method on confluence service**


[775261ac57f9505](https://github.com/bsorrentino/maven-confluence-plugin/commit/775261ac57f9505) bsorrentino *2021-04-30 17:30:49*

**refactor deploy state management**


[cd6bbd799f7a257](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd6bbd799f7a257) bsorrentino *2021-04-30 15:49:46*

**Merge branch 'feature/issue250' into develop**


[924e6d3e327be5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/924e6d3e327be5b) bsorrentino *2021-04-30 14:08:15*

**issue #250 - clear deploystate on delete mojo**


[7072c8741497450](https://github.com/bsorrentino/maven-confluence-plugin/commit/7072c8741497450) bsorrentino *2021-04-30 14:07:55*

**issue #250 - clear deploystate on delete mojo**


[77706af405a845b](https://github.com/bsorrentino/maven-confluence-plugin/commit/77706af405a845b) bsorrentino *2021-04-30 14:07:41*

**Merge branch 'feature/improve_page_update_check' into develop**


[d7b156709789fcc](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7b156709789fcc) bsorrentino *2021-04-30 10:08:46*

**clean code**


[495ed872f9df349](https://github.com/bsorrentino/maven-confluence-plugin/commit/495ed872f9df349) bsorrentino *2021-04-30 10:08:14*

**fix newPage method**


[eddebffe46cf7fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/eddebffe46cf7fb) bsorrentino *2021-04-29 21:17:06*

**fix newPage method**


[dd98d097aac082b](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd98d097aac082b) bsorrentino *2021-04-29 21:16:03*

**manage extra attribute in deployStateManager**


[595847e4a185233](https://github.com/bsorrentino/maven-confluence-plugin/commit/595847e4a185233) bsorrentino *2021-04-29 18:37:18*

**add TypesDefinition interface**


[f007ccd3f9e525b](https://github.com/bsorrentino/maven-confluence-plugin/commit/f007ccd3f9e525b) bsorrentino *2021-04-29 16:16:47*

**update action**


[bb399bfb93e8925](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb399bfb93e8925) bsorrentino *2021-04-29 10:37:42*

**rename module commons.io to org.apache.commons.io**


[a85d50509cc1646](https://github.com/bsorrentino/maven-confluence-plugin/commit/a85d50509cc1646) bsorrentino *2021-04-29 10:35:32*

**Merge branch 'dependabot/maven/commons-io-commons-io-2.7' into develop**


[8a18fd8d69dc36e](https://github.com/bsorrentino/maven-confluence-plugin/commit/8a18fd8d69dc36e) bsorrentino *2021-04-29 10:22:19*

**Merge branch 'develop' into dependabot/maven/commons-io-commons-io-2.7**


[cc13f3216d8c57d](https://github.com/bsorrentino/maven-confluence-plugin/commit/cc13f3216d8c57d) bsorrentino *2021-04-29 08:58:34*

**Bump commons-io from 1.4 to 2.7**

 * Bumps commons-io from 1.4 to 2.7.
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[ef35e0c843605d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef35e0c843605d7) dependabot[bot] *2021-04-26 20:12:54*

**moved to next development version**


[0ebafb77fbe056f](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ebafb77fbe056f) bsorrentino *2021-03-07 17:50:39*

**Merge tag 'v7.0-rc2' into develop**

 * release 7.0-rc2

[9f6e69feb284ccd](https://github.com/bsorrentino/maven-confluence-plugin/commit/9f6e69feb284ccd) bsorrentino *2021-03-07 17:47:56*


## v7.0-rc2
### Generic changes

**Merge branch 'release/7.0-rc2'**


[9cbee393b049cbd](https://github.com/bsorrentino/maven-confluence-plugin/commit/9cbee393b049cbd) bsorrentino *2021-03-07 17:47:34*

**update readme**


[9d23ab5acd751c8](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d23ab5acd751c8) bsorrentino *2021-03-07 17:46:52*

**prepare for release**


[5b73edec7fada64](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b73edec7fada64) bsorrentino *2021-02-23 18:33:05*

**removed htmlinline output debug info**


[14dba471b8fb50e](https://github.com/bsorrentino/maven-confluence-plugin/commit/14dba471b8fb50e) bsorrentino *2021-02-17 11:11:03*

**set 'name' attribute as required**


[2e70827f3f9c7d6](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e70827f3f9c7d6) bsorrentino *2021-02-17 11:10:23*

**issue #245 - Improved code to load content from classpath. Test refinements**


[6c470fc5545741f](https://github.com/bsorrentino/maven-confluence-plugin/commit/6c470fc5545741f) bsorrentino *2021-02-14 20:20:07*

**Merge branch 'master' into develop**


[c338e665ad53e33](https://github.com/bsorrentino/maven-confluence-plugin/commit/c338e665ad53e33) bsorrentino *2021-02-11 19:20:32*

**Merge branch 'hotfix/actions'**


[d0b27d616f4a5eb](https://github.com/bsorrentino/maven-confluence-plugin/commit/d0b27d616f4a5eb) bsorrentino *2021-02-11 19:20:29*

**update actions**


[d8082376be22051](https://github.com/bsorrentino/maven-confluence-plugin/commit/d8082376be22051) bsorrentino *2021-02-11 19:20:15*

**updated to dev version**


[da74ad4680dcd96](https://github.com/bsorrentino/maven-confluence-plugin/commit/da74ad4680dcd96) bsorrentino *2021-02-11 19:16:57*

**Merge branch 'master' into develop**


[95d6f21cf27970e](https://github.com/bsorrentino/maven-confluence-plugin/commit/95d6f21cf27970e) bsorrentino *2021-02-11 19:15:45*

**Merge branch 'hotfix/actions'**


[74961269438bfaf](https://github.com/bsorrentino/maven-confluence-plugin/commit/74961269438bfaf) bsorrentino *2021-02-11 19:15:42*

**update actions**


[4c86cc3c8e14e02](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c86cc3c8e14e02) bsorrentino *2021-02-11 19:15:30*

**update actions**


[58963e711ed6fc3](https://github.com/bsorrentino/maven-confluence-plugin/commit/58963e711ed6fc3) bsorrentino *2021-02-11 19:04:30*

**Merge tag 'v7.0-rc1' into develop**

 * new release

[5705e857e3f44ce](https://github.com/bsorrentino/maven-confluence-plugin/commit/5705e857e3f44ce) bsorrentino *2021-02-11 18:57:03*

**issue #244 - fixed export packages (Java9 Modules)**


[60ddbdca06b1958](https://github.com/bsorrentino/maven-confluence-plugin/commit/60ddbdca06b1958) bsorrentino *2021-02-11 16:57:16*

**issue #244 - fixed export packages (Java9 Modules)**


[688bfe292a07690](https://github.com/bsorrentino/maven-confluence-plugin/commit/688bfe292a07690) bsorrentino *2021-02-11 16:57:07*

**update actions**


[828777f3d2e28fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/828777f3d2e28fe) bsorrentino *2021-02-11 16:43:33*

**skipped itest module**


[46a9d67d758fe5f](https://github.com/bsorrentino/maven-confluence-plugin/commit/46a9d67d758fe5f) bsorrentino *2021-02-11 16:37:16*

**update actions**


[c8fa61726f60347](https://github.com/bsorrentino/maven-confluence-plugin/commit/c8fa61726f60347) bsorrentino *2021-02-11 16:31:42*

**clean code**


[76828e6f91bf45d](https://github.com/bsorrentino/maven-confluence-plugin/commit/76828e6f91bf45d) bsorrentino *2021-02-11 16:27:56*

**Merge branch 'feature/issue244' into develop**


[e47aa5986632391](https://github.com/bsorrentino/maven-confluence-plugin/commit/e47aa5986632391) bsorrentino *2021-02-11 14:34:50*

**issue #244 - Updated test for freemarker**


[99c4df21dec8989](https://github.com/bsorrentino/maven-confluence-plugin/commit/99c4df21dec8989) bsorrentino *2021-02-11 14:33:50*

**issue #244 - make freemarker preprocessor module compliant with JDK9 (refers to issue #224)**


[76c7434e57edf11](https://github.com/bsorrentino/maven-confluence-plugin/commit/76c7434e57edf11) bsorrentino *2021-02-11 11:25:39*

**moved to development version**


[fac21e2520efd65](https://github.com/bsorrentino/maven-confluence-plugin/commit/fac21e2520efd65) Build Pipeline *2021-02-10 16:10:50*

**Merge tag 'v7.0-beta2' into develop**

 * new release

[8773b19f1c7255e](https://github.com/bsorrentino/maven-confluence-plugin/commit/8773b19f1c7255e) bsorrentino *2021-02-09 16:53:05*


## v7.0-rc1
### Generic changes

**Merge branch 'release/7.0-rc1'**


[9325ab997628d50](https://github.com/bsorrentino/maven-confluence-plugin/commit/9325ab997628d50) bsorrentino *2021-02-11 18:56:53*

**changelog updated**


[e1fbce5bd8f3309](https://github.com/bsorrentino/maven-confluence-plugin/commit/e1fbce5bd8f3309) bsorrentino *2021-02-11 18:56:30*

**moved to release version**


[abdb5ba5d525580](https://github.com/bsorrentino/maven-confluence-plugin/commit/abdb5ba5d525580) bsorrentino *2021-02-11 18:55:09*

**update documentation**


[cead3d4206fcc58](https://github.com/bsorrentino/maven-confluence-plugin/commit/cead3d4206fcc58) bsorrentino *2021-02-11 18:54:41*

**update readme**


[449a4eeede1ab60](https://github.com/bsorrentino/maven-confluence-plugin/commit/449a4eeede1ab60) bsorrentino *2021-02-11 18:54:23*

**added publish site aciton**


[4d79f7464a7ef77](https://github.com/bsorrentino/maven-confluence-plugin/commit/4d79f7464a7ef77) bsorrentino *2021-02-11 18:46:47*


## v7.0-beta2
### Generic changes

**Merge branch 'release/7.0-beta2'**


[7941563e8368a0f](https://github.com/bsorrentino/maven-confluence-plugin/commit/7941563e8368a0f) bsorrentino *2021-02-09 16:52:54*

**update readme**


[b2bb1a44ba3e3c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/b2bb1a44ba3e3c3) bsorrentino *2021-02-09 16:52:31*

**update changelog**


[d4c23ad8de1aa84](https://github.com/bsorrentino/maven-confluence-plugin/commit/d4c23ad8de1aa84) bsorrentino *2021-02-09 16:43:22*

**updated release to 7.0-beta2**


[e90e8efa245411f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e90e8efa245411f) bsorrentino *2021-02-09 16:41:27*

**removed warning 'Unchecked cast'**


[17d749152dbc9df](https://github.com/bsorrentino/maven-confluence-plugin/commit/17d749152dbc9df) bsorrentino *2021-02-09 16:20:53*

**clean code**


[1d95a76efbce875](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d95a76efbce875) bsorrentino *2021-02-08 18:55:43*

**updated docker compose to support confluence 7.6**


[93ba4523d31df4e](https://github.com/bsorrentino/maven-confluence-plugin/commit/93ba4523d31df4e) bsorrentino *2021-02-07 15:33:23*

**Merge branch 'feature/issue242' into develop**


[10e791a322a7ca1](https://github.com/bsorrentino/maven-confluence-plugin/commit/10e791a322a7ca1) bsorrentino *2021-02-07 15:21:50*

**Merge branch '3a04huk-bugfix/#242-add-i18n-to-renderers' into feature/issue242**


[d2ba9118f6c58d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/d2ba9118f6c58d1) bsorrentino *2021-02-07 15:20:32*

**Merge branch 'develop' into issue241**


[aabaa571dace224](https://github.com/bsorrentino/maven-confluence-plugin/commit/aabaa571dace224) bsorrentino *2021-02-07 14:44:28*

**issue #239 - updated mojo timeout property names**


[140047bda4fe353](https://github.com/bsorrentino/maven-confluence-plugin/commit/140047bda4fe353) bsorrentino *2021-02-07 14:20:06*

**issue #239 - added parameters to mojos**


[a8a2a9f034910e8](https://github.com/bsorrentino/maven-confluence-plugin/commit/a8a2a9f034910e8) bsorrentino *2021-02-07 14:17:02*

**issue #239 - added support for timeout parameters**


[c2c30d1aabdf9bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/c2c30d1aabdf9bb) bsorrentino *2021-02-07 12:27:27*

**issue #339 - moved httpclient dep in xmlrpc module**


[dd7cc15a15e7fce](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd7cc15a15e7fce) bsorrentino *2021-02-07 12:25:46*

**issue #241 - removed confluence service classes from core**


[3903423485a7a13](https://github.com/bsorrentino/maven-confluence-plugin/commit/3903423485a7a13) bsorrentino *2021-02-07 12:25:24*

**issue #241 - added module rest and xmlrpc to better manage service layer**


[aee5fa602d6b488](https://github.com/bsorrentino/maven-confluence-plugin/commit/aee5fa602d6b488) bsorrentino *2021-02-07 12:20:44*

**issue #239 - added module rest and xmlrpc to better manage service layer**


[1e7bab24ef0a95e](https://github.com/bsorrentino/maven-confluence-plugin/commit/1e7bab24ef0a95e) bsorrentino *2021-02-07 12:16:11*

**fix issue #242**


[2f2ca0f784fabf6](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f2ca0f784fabf6) 3a04huk *2021-02-07 11:54:27*

**issue #239 - updated mojo timeout property names**


[aef9cea8c862568](https://github.com/bsorrentino/maven-confluence-plugin/commit/aef9cea8c862568) bsorrentino *2021-02-07 11:46:37*

**Merge branch 'feature/issue239' into develop**


[ee903c062c7719a](https://github.com/bsorrentino/maven-confluence-plugin/commit/ee903c062c7719a) bsorrentino *2021-02-07 11:36:05*

**issue #239 - added parameters to mojos**


[af111f0aec2cdcf](https://github.com/bsorrentino/maven-confluence-plugin/commit/af111f0aec2cdcf) bsorrentino *2021-02-07 11:34:59*

**issue #239 - added support for timeout parameters**


[d1346acb2f22475](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1346acb2f22475) bsorrentino *2021-02-07 11:34:30*

**issue #339 - moved httpclient dep in xmlrpc module**


[20cda6a28efd409](https://github.com/bsorrentino/maven-confluence-plugin/commit/20cda6a28efd409) bsorrentino *2021-02-06 19:14:27*

**issue #239 - added module rest and xmlrpc to better manage service layer**


[79f6e4239c4f56a](https://github.com/bsorrentino/maven-confluence-plugin/commit/79f6e4239c4f56a) bsorrentino *2021-02-06 18:56:17*

**issue #239 - added module rest and xmlrpc to better manage service layer**


[287d3be0cfc4190](https://github.com/bsorrentino/maven-confluence-plugin/commit/287d3be0cfc4190) bsorrentino *2021-02-06 18:56:07*

**issue #239 - Started to integrate anchor**


[c6557edf51fa377](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6557edf51fa377) Build Pipeline *2021-02-05 19:01:29*

**issue #239 - added Anchor to the site model as alternative to Home**


[2210fafa669a808](https://github.com/bsorrentino/maven-confluence-plugin/commit/2210fafa669a808) bsorrentino *2021-02-05 12:58:06*

**issue #239 - started implementing Site.Anchor element**


[d659e9b0d32bb35](https://github.com/bsorrentino/maven-confluence-plugin/commit/d659e9b0d32bb35) Build Pipeline *2021-02-04 18:22:15*

**clean code**


[e04a3c705d635ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/e04a3c705d635ed) bsorrentino *2021-01-31 22:23:08*

**Merge branch 'feature/export_refactoring' into develop**


[2ce83d4a5929d29](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ce83d4a5929d29) bsorrentino *2021-01-31 18:15:34*

**remove exportPage method from ConfluenceService**


[630f872dc2c367c](https://github.com/bsorrentino/maven-confluence-plugin/commit/630f872dc2c367c) bsorrentino *2021-01-31 18:15:21*

**remove exportPage method from ConfluenceService**


[2add1ab75968649](https://github.com/bsorrentino/maven-confluence-plugin/commit/2add1ab75968649) bsorrentino *2021-01-31 18:15:09*

**Merge branch 'feature/issue240' into develop**


[69a30384c959ea7](https://github.com/bsorrentino/maven-confluence-plugin/commit/69a30384c959ea7) bsorrentino *2021-01-27 19:05:34*

**issue #240 - Updated DeployStateManager to manage extra attribute in addition to 'hash'**


[3d93f3ce6b6bc09](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d93f3ce6b6bc09) bsorrentino *2021-01-27 18:56:14*

**initialized java.util.logging default level**


[582623e62ce23bc](https://github.com/bsorrentino/maven-confluence-plugin/commit/582623e62ce23bc) bsorrentino *2021-01-27 18:53:27*

**issue #240 - Updated DeployStateManager to manage directly Page and Attachment instead of URI**


[0fb6a40c5853029](https://github.com/bsorrentino/maven-confluence-plugin/commit/0fb6a40c5853029) bsorrentino *2021-01-26 22:32:32*

**added defaultFileExt attribute to Site model**

 * clean code

[e9b65a608deb2bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/e9b65a608deb2bd) bsorrentino *2021-01-22 19:05:53*

**optimized attachments pubblication process making check for skip faster**


[bc9a95b41f7a570](https://github.com/bsorrentino/maven-confluence-plugin/commit/bc9a95b41f7a570) bsorrentino *2021-01-22 16:30:26*

**Merge branch 'master' into develop**


[21ec2d43b416ce9](https://github.com/bsorrentino/maven-confluence-plugin/commit/21ec2d43b416ce9) bsorrentino *2021-01-03 18:10:12*

**Merge branch 'hotfix/site'**


[e1b39e61f309fff](https://github.com/bsorrentino/maven-confluence-plugin/commit/e1b39e61f309fff) bsorrentino *2021-01-03 18:10:07*

**created: shell script to deploy site to github pages**


[a3f400d74ebe379](https://github.com/bsorrentino/maven-confluence-plugin/commit/a3f400d74ebe379) bsorrentino *2021-01-03 18:03:40*

**Merge branch 'master' into develop**


[6f4fdc89e361aea](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f4fdc89e361aea) bsorrentino *2021-01-03 17:22:23*

**Merge branch 'hotfix/actions'**


[432d115e24948f4](https://github.com/bsorrentino/maven-confluence-plugin/commit/432d115e24948f4) bsorrentino *2021-01-03 17:22:21*

**Merge remote-tracking branch 'origin/master'**


[1647f5b9a325185](https://github.com/bsorrentino/maven-confluence-plugin/commit/1647f5b9a325185) bsorrentino *2021-01-03 17:22:16*

**Merge remote-tracking branch 'origin/master' into hotfix/actions**


[d0d938b97405241](https://github.com/bsorrentino/maven-confluence-plugin/commit/d0d938b97405241) bsorrentino *2021-01-03 17:21:20*

**Merge branch 'master' into develop**


[46ebd471dbdf852](https://github.com/bsorrentino/maven-confluence-plugin/commit/46ebd471dbdf852) bsorrentino *2021-01-03 17:18:48*

**Merge branch 'hotfix/actions'**


[8751cfa50f77e88](https://github.com/bsorrentino/maven-confluence-plugin/commit/8751cfa50f77e88) bsorrentino *2021-01-03 17:18:45*

**disable docs job**


[ecdd02cebe345ce](https://github.com/bsorrentino/maven-confluence-plugin/commit/ecdd02cebe345ce) bsorrentino *2021-01-03 17:18:08*

**update deploy.yaml**


[58bf8fd34be0300](https://github.com/bsorrentino/maven-confluence-plugin/commit/58bf8fd34be0300) bsorrentino *2021-01-03 17:12:21*

**update deploy.yaml**


[fae49d9c13aadc9](https://github.com/bsorrentino/maven-confluence-plugin/commit/fae49d9c13aadc9) bsorrentino *2021-01-03 17:04:20*

**update deploy.yaml**


[d26c1cecf2996a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/d26c1cecf2996a5) bsorrentino *2021-01-03 17:00:41*

**update deploy.yaml**


[815ec06f0809840](https://github.com/bsorrentino/maven-confluence-plugin/commit/815ec06f0809840) bsorrentino *2021-01-03 16:51:22*

**Merge pull request #237 from bsorrentino/bsorrentino-patch-1**

 * update deploy.yaml

[5e587b9a7bba55f](https://github.com/bsorrentino/maven-confluence-plugin/commit/5e587b9a7bba55f) bsorrentino *2021-01-03 16:47:16*

**update deploy.yaml**


[7c4804db8e30d8e](https://github.com/bsorrentino/maven-confluence-plugin/commit/7c4804db8e30d8e) bsorrentino *2021-01-03 16:43:09*

**moved to next developer release**


[7e1409d99f2c410](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e1409d99f2c410) bsorrentino *2021-01-03 16:29:54*

**Merge tag 'v7.0-beta1' into develop**

 * new candidate release

[858bb2fce522525](https://github.com/bsorrentino/maven-confluence-plugin/commit/858bb2fce522525) bsorrentino *2021-01-03 16:28:58*


## v7.0-beta1
### Generic changes

**Merge branch 'release/7.0-beta1'**


[5f8388cbdc7ae15](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f8388cbdc7ae15) bsorrentino *2021-01-03 16:28:41*

**update changelog**


[01ce7a875a5539e](https://github.com/bsorrentino/maven-confluence-plugin/commit/01ce7a875a5539e) bsorrentino *2021-01-03 16:28:12*

**updated documentation**


[6177b384190b04e](https://github.com/bsorrentino/maven-confluence-plugin/commit/6177b384190b04e) bsorrentino *2021-01-03 16:27:47*

**add action for deploy new version to sonatype server**


[f0af742eded5f48](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0af742eded5f48) bsorrentino *2021-01-03 16:27:25*

**add action for deploy new version to sonatype server**


[d62168c8eea976b](https://github.com/bsorrentino/maven-confluence-plugin/commit/d62168c8eea976b) bsorrentino *2021-01-03 14:04:29*

**updated version to the next release**


[01fb3e278f6a71a](https://github.com/bsorrentino/maven-confluence-plugin/commit/01fb3e278f6a71a) bsorrentino *2021-01-03 13:50:30*

**clean code**


[ec061e2288f7cc8](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec061e2288f7cc8) bsorrentino *2020-12-15 12:31:58*

**fixed issue: DeployStateManaged save the hash also on deploy failure**


[b648dd0507f3db0](https://github.com/bsorrentino/maven-confluence-plugin/commit/b648dd0507f3db0) bsorrentino *2020-12-14 18:55:44*

**issue #224 - fixed load markdown processor lifecycle**


[d3958c3ad9a0547](https://github.com/bsorrentino/maven-confluence-plugin/commit/d3958c3ad9a0547) bsorrentino *2020-12-14 10:04:15*

**added: action compiles on jdk 9**


[7fa9b39a19e6a60](https://github.com/bsorrentino/maven-confluence-plugin/commit/7fa9b39a19e6a60) bsorrentino *2020-12-13 23:33:48*

**added: support for plugin  variables ${...} in markdown**


[bfbb355fdfd89fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/bfbb355fdfd89fe) bsorrentino *2020-12-13 23:30:33*

**removed: metainf.services module**


[00bc08237ea307e](https://github.com/bsorrentino/maven-confluence-plugin/commit/00bc08237ea307e) bsorrentino *2020-12-13 22:59:11*

**added SPI META-INF service implementation info**


[9d9080c1baeb122](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d9080c1baeb122) bsorrentino *2020-12-13 22:55:46*

**removed metainf-services annotation**


[8bcf592b175830d](https://github.com/bsorrentino/maven-confluence-plugin/commit/8bcf592b175830d) bsorrentino *2020-12-13 22:55:03*

**added multirelease jar support in maven**

 * removed metainf-services processor

[9d07de394d3ec9a](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d07de394d3ec9a) bsorrentino *2020-12-13 22:54:26*

**removed jgitflow-maven-plugin added version 3.2.0 of maven-jar-plugin**


[2dff3dafb122d91](https://github.com/bsorrentino/maven-confluence-plugin/commit/2dff3dafb122d91) bsorrentino *2020-12-13 22:53:39*

**add multirelease jar support in maven**


[c8971587ce847b5](https://github.com/bsorrentino/maven-confluence-plugin/commit/c8971587ce847b5) bsorrentino *2020-12-13 22:51:24*

**add multirelease jar support in maven**


[f6d8553a688796f](https://github.com/bsorrentino/maven-confluence-plugin/commit/f6d8553a688796f) bsorrentino *2020-12-13 22:50:54*

**set 'commonmark' as default markdown processor. pegdown is deprecated**


[b53c05a073451ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/b53c05a073451ff) bsorrentino *2020-12-13 21:33:29*

**Merge branch 'feature/issue224' into develop**


[f1bca73df88a025](https://github.com/bsorrentino/maven-confluence-plugin/commit/f1bca73df88a025) bsorrentino *2020-12-13 21:23:01*

**clean code**


[7e1917a0471cd6a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e1917a0471cd6a) bsorrentino *2020-12-13 21:22:55*

**issue #224 - Removed jaxb-xml-impl in favour of jackson-xml-impl**


[72c75d41960e7c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/72c75d41960e7c5) bsorrentino *2020-12-13 21:20:43*

**issue #224 - added support for multi version jar**


[40a402a072dc73d](https://github.com/bsorrentino/maven-confluence-plugin/commit/40a402a072dc73d) bsorrentino *2020-12-13 19:33:16*

**fixed problem on delete when home.name attribute in site is not set**


[1680a5dc0527eb1](https://github.com/bsorrentino/maven-confluence-plugin/commit/1680a5dc0527eb1) bsorrentino *2020-12-13 19:30:04*

**issue #224 - Added support for java9 module**


[229cbc511025784](https://github.com/bsorrentino/maven-confluence-plugin/commit/229cbc511025784) bsorrentino *2020-12-12 14:48:56*

**pegdown processor is removed by build - end of support**


[829fc2da16510ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/829fc2da16510ed) bsorrentino *2020-12-12 11:43:23*

**issue #224 - Added support of java9 module**


[88fb15d1fd52087](https://github.com/bsorrentino/maven-confluence-plugin/commit/88fb15d1fd52087) bsorrentino *2020-12-12 11:38:17*

**Merge branch 'master' into develop**


[92557b8c61811ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/92557b8c61811ec) bsorrentino *2020-12-11 19:56:58*

**Merge branch 'hotfix/changelog'**


[4f234a8c71efac9](https://github.com/bsorrentino/maven-confluence-plugin/commit/4f234a8c71efac9) bsorrentino *2020-12-11 19:56:55*

**update changelog**


[e5567affd120a1c](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5567affd120a1c) bsorrentino *2020-12-11 19:56:45*

**move to next development version**


[23de6c44ec10aeb](https://github.com/bsorrentino/maven-confluence-plugin/commit/23de6c44ec10aeb) bsorrentino *2020-12-11 19:25:22*

**Merge tag 'v6.20' into develop**

 * new release 6.20

[436d2f39f315428](https://github.com/bsorrentino/maven-confluence-plugin/commit/436d2f39f315428) bsorrentino *2020-12-11 19:21:42*


## v6.20
### Generic changes

**Merge branch 'release/6.20'**


[97ca7a83d23b4b5](https://github.com/bsorrentino/maven-confluence-plugin/commit/97ca7a83d23b4b5) bsorrentino *2020-12-11 19:21:28*

**update site publish message**


[cbf82a9d2648f2f](https://github.com/bsorrentino/maven-confluence-plugin/commit/cbf82a9d2648f2f) bsorrentino *2020-12-11 19:13:29*

**set release, update changelog and readme**


[ce66f9024e2fb92](https://github.com/bsorrentino/maven-confluence-plugin/commit/ce66f9024e2fb92) bsorrentino *2020-12-11 18:52:21*

**issue #235 - update site documentation**


[67c687bc064425d](https://github.com/bsorrentino/maven-confluence-plugin/commit/67c687bc064425d) bsorrentino *2020-12-11 18:40:44*

**issue #235 - fix problem with indented macro**


[d4f9253a5aafe7f](https://github.com/bsorrentino/maven-confluence-plugin/commit/d4f9253a5aafe7f) bsorrentino *2020-12-11 17:19:14*

**issue #235 - unit test refinements**


[4381bc94014cb86](https://github.com/bsorrentino/maven-confluence-plugin/commit/4381bc94014cb86) bsorrentino *2020-12-11 16:31:59*

**Merge branch 'feature/issue235' into develop**


[1ca53d8ef6025bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/1ca53d8ef6025bb) bsorrentino *2020-12-11 14:12:44*

**Merge branch 'develop' into feature/issue235**


[d306781cce04ac3](https://github.com/bsorrentino/maven-confluence-plugin/commit/d306781cce04ac3) bsorrentino *2020-12-11 14:09:52*

**Merge branch 'master' into develop**


[63644f7658d9af6](https://github.com/bsorrentino/maven-confluence-plugin/commit/63644f7658d9af6) bsorrentino *2020-12-11 14:08:36*

**Merge branch 'hotfix/doc'**


[c5b5fef8a56facf](https://github.com/bsorrentino/maven-confluence-plugin/commit/c5b5fef8a56facf) bsorrentino *2020-12-11 14:08:33*

**updated changelog**

 * updated readme

[2ca1d0dcfb98fa8](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ca1d0dcfb98fa8) bsorrentino *2020-12-11 14:08:01*

**issue #235 - added support of declaring confluence macro inside html comment. Removed confluence macro detection skipping the escaping of markdown text**


[957a048c046c94b](https://github.com/bsorrentino/maven-confluence-plugin/commit/957a048c046c94b) bsorrentino *2020-12-11 14:01:55*

**add confluence {chidren} macro support**


[9ad656017ed97af](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ad656017ed97af) bsorrentino *2020-12-09 11:48:45*

**move to next development version**


[ec8800fe3714a7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec8800fe3714a7e) bsorrentino *2020-11-27 17:34:17*

**add newline when detect markdown softlinebreak**


[2c7c9d2b5848ae4](https://github.com/bsorrentino/maven-confluence-plugin/commit/2c7c9d2b5848ae4) bsorrentino *2020-11-27 17:02:03*

**skip escape markdown text for {toc} tests**


[4c8884165a91489](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c8884165a91489) bsorrentino *2020-11-27 17:01:17*

**skip escape markdown text for {toc}**


[dad3f5903e43c26](https://github.com/bsorrentino/maven-confluence-plugin/commit/dad3f5903e43c26) bsorrentino *2020-11-27 17:00:04*

**clean code**


[83c16155a4561bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/83c16155a4561bb) bsorrentino *2020-11-26 18:01:38*

**rename folder**


[6d4605bac69474c](https://github.com/bsorrentino/maven-confluence-plugin/commit/6d4605bac69474c) bsorrentino *2020-11-26 17:45:27*

**rename folder**


[4cf552f7db81f7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/4cf552f7db81f7b) bsorrentino *2020-11-26 17:42:20*

**fix junit5 usage**


[fe17dcc3f304022](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe17dcc3f304022) bsorrentino *2020-11-26 17:27:38*

**add junit platfrom runner**


[ccaa5f0d0c61e4b](https://github.com/bsorrentino/maven-confluence-plugin/commit/ccaa5f0d0c61e4b) bsorrentino *2020-11-26 17:15:49*

**update pipeline**


[85a8b170bbadf96](https://github.com/bsorrentino/maven-confluence-plugin/commit/85a8b170bbadf96) bsorrentino *2020-11-26 16:59:38*

**Merge branch 'feature/newlayout' into develop**


[09d87b5425841ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/09d87b5425841ec) bsorrentino *2020-11-26 16:34:34*

**update pipeline**


[a49d60c813e582e](https://github.com/bsorrentino/maven-confluence-plugin/commit/a49d60c813e582e) bsorrentino *2020-11-26 16:34:17*

**add pipeline for deploy snapshot**


[6d1a522cf2c5321](https://github.com/bsorrentino/maven-confluence-plugin/commit/6d1a522cf2c5321) bsorrentino *2020-11-26 16:32:09*

**refine project layout**

 * clean code

[97904fa1ac854c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/97904fa1ac854c1) bsorrentino *2020-11-26 16:31:35*

**remove forge submodule**


[a6e856d2fe97ea5](https://github.com/bsorrentino/maven-confluence-plugin/commit/a6e856d2fe97ea5) bsorrentino *2020-11-26 14:04:23*

**rename folders**


[e4dc78f6f58dd8e](https://github.com/bsorrentino/maven-confluence-plugin/commit/e4dc78f6f58dd8e) bsorrentino *2020-11-26 13:56:19*

**extract a new gitlog+jira module**


[96b39de7984c982](https://github.com/bsorrentino/maven-confluence-plugin/commit/96b39de7984c982) bsorrentino *2020-11-25 22:34:29*

**move to nex developer version**


[ae08d17763354d3](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae08d17763354d3) bsorrentino *2020-11-03 10:06:33*

**Merge tag 'v6.11' into develop**

 * merge PR #230 #231 #232

[3a35f3773b1696d](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a35f3773b1696d) bartolomeo sorrentino *2020-11-02 10:36:59*


## v6.11
### Generic changes

**Merge branch 'release/6.11'**


[961c57c3da75228](https://github.com/bsorrentino/maven-confluence-plugin/commit/961c57c3da75228) bartolomeo sorrentino *2020-11-02 10:36:29*

**move to next release**


[7f3eca2a2197142](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f3eca2a2197142) bartolomeo sorrentino *2020-11-02 10:34:32*

**Merge branch 'dependabot/maven/junit-junit-4.13.1' into release/6.11**


[ae3afeeabea367d](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae3afeeabea367d) bartolomeo sorrentino *2020-11-02 10:25:00*

**Merge branch 'dependabot/maven/maven-confluence-itest/src/test/resources/simple-plugin-project/junit-junit-4.13.1' into release/6.11**


[52b9bbb9c67658b](https://github.com/bsorrentino/maven-confluence-plugin/commit/52b9bbb9c67658b) bartolomeo sorrentino *2020-11-02 10:24:04*

**Merge branch 'dependabot/maven/maven-confluence-itest/src/test/resources/plugin-project-goals-in-subpage/junit-junit-4.13.1' into release/6.11**


[8afb392a2b87420](https://github.com/bsorrentino/maven-confluence-plugin/commit/8afb392a2b87420) bartolomeo sorrentino *2020-11-02 10:22:57*

**Bump junit from 4.12 to 4.13.1**

 * Bumps [junit](https://github.com/junit-team/junit4) from 4.12 to 4.13.1.
 * - [Release notes](https://github.com/junit-team/junit4/releases)
 * - [Changelog](https://github.com/junit-team/junit4/blob/main/doc/ReleaseNotes4.12.md)
 * - [Commits](https://github.com/junit-team/junit4/compare/r4.12...r4.13.1)
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[df69c834dcf69b7](https://github.com/bsorrentino/maven-confluence-plugin/commit/df69c834dcf69b7) dependabot[bot] *2020-10-13 06:49:17*

**Bump junit**

 * Bumps [junit](https://github.com/junit-team/junit4) from 4.11 to 4.13.1.
 * - [Release notes](https://github.com/junit-team/junit4/releases)
 * - [Changelog](https://github.com/junit-team/junit4/blob/main/doc/ReleaseNotes4.11.md)
 * - [Commits](https://github.com/junit-team/junit4/compare/r4.11...r4.13.1)
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[2347b45e8258555](https://github.com/bsorrentino/maven-confluence-plugin/commit/2347b45e8258555) dependabot[bot] *2020-10-12 17:51:37*

**Bump junit**

 * Bumps [junit](https://github.com/junit-team/junit4) from 4.11 to 4.13.1.
 * - [Release notes](https://github.com/junit-team/junit4/releases)
 * - [Changelog](https://github.com/junit-team/junit4/blob/main/doc/ReleaseNotes4.11.md)
 * - [Commits](https://github.com/junit-team/junit4/compare/r4.11...r4.13.1)
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[c8c7efeab6bae49](https://github.com/bsorrentino/maven-confluence-plugin/commit/c8c7efeab6bae49) dependabot[bot] *2020-10-12 17:51:36*

**Merge remote-tracking branch 'origin/develop' into develop**


[dbf22cbe711812c](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbf22cbe711812c) bartolomeo sorrentino *2020-10-06 09:36:57*

**verified issue #227**


[91a1d4555ae2e41](https://github.com/bsorrentino/maven-confluence-plugin/commit/91a1d4555ae2e41) bartolomeo sorrentino *2020-10-06 09:36:13*

**Merge branch 'master' into develop**


[a3b08507759dc5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/a3b08507759dc5b) bsorrentino *2020-09-25 18:22:40*

**Merge branch 'hotfix/changelog'**


[6c553d15ddaf063](https://github.com/bsorrentino/maven-confluence-plugin/commit/6c553d15ddaf063) bsorrentino *2020-09-25 18:22:37*

**update changelog**


[df7b5ce5b1ee24e](https://github.com/bsorrentino/maven-confluence-plugin/commit/df7b5ce5b1ee24e) bsorrentino *2020-09-25 18:22:27*

**Merge tag 'v6.10' into develop**

 * fix issue #229

[e54c04f1b9ecc9f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e54c04f1b9ecc9f) bartolomeo sorrentino *2020-09-25 18:06:23*


## v6.10
### Generic changes

**Merge branch 'release/6.10'**


[66fdc36852092fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/66fdc36852092fe) bartolomeo sorrentino *2020-09-25 18:06:04*

**update readme**


[c4f7c407a6c8846](https://github.com/bsorrentino/maven-confluence-plugin/commit/c4f7c407a6c8846) bartolomeo sorrentino *2020-09-25 17:17:34*

**set new release version**


[8b7996a20f3367d](https://github.com/bsorrentino/maven-confluence-plugin/commit/8b7996a20f3367d) bartolomeo sorrentino *2020-09-25 17:16:01*

**#229 - fix storage format extension name**


[ed270f5de9afe15](https://github.com/bsorrentino/maven-confluence-plugin/commit/ed270f5de9afe15) bartolomeo sorrentino *2020-09-25 17:12:58*

**move to next development version**


[0dfd70e94e8d08c](https://github.com/bsorrentino/maven-confluence-plugin/commit/0dfd70e94e8d08c) bsorrentino *2020-09-23 09:33:24*

**Merge branch 'master' into develop**


[2df75e4eb3ebcd1](https://github.com/bsorrentino/maven-confluence-plugin/commit/2df75e4eb3ebcd1) bsorrentino *2020-09-19 20:13:52*

**Merge branch 'hotfix/doc'**


[0cc33ae62526e68](https://github.com/bsorrentino/maven-confluence-plugin/commit/0cc33ae62526e68) bsorrentino *2020-09-19 20:13:49*

**update changelog**


[925ba432a77c557](https://github.com/bsorrentino/maven-confluence-plugin/commit/925ba432a77c557) bsorrentino *2020-09-19 20:13:38*

**Merge tag 'v6.9.1' into develop**

 * new release

[d7cf412af0c5d49](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7cf412af0c5d49) bsorrentino *2020-09-19 20:08:48*

**move to next development version**


[c464f8c0d626aac](https://github.com/bsorrentino/maven-confluence-plugin/commit/c464f8c0d626aac) bsorrentino *2020-08-20 08:42:46*


## v6.9.1
### Generic changes

**Merge branch 'release/6.9.1'**


[8128f1530c42037](https://github.com/bsorrentino/maven-confluence-plugin/commit/8128f1530c42037) bsorrentino *2020-09-19 20:05:35*

**update changelog**


[fa36e005073104e](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa36e005073104e) bartolomeo sorrentino *2020-09-18 12:45:57*

**update readme**


[a40f8ddbb46bb9e](https://github.com/bsorrentino/maven-confluence-plugin/commit/a40f8ddbb46bb9e) bartolomeo sorrentino *2020-09-18 12:44:23*

**move to next release**


[7749f60f448dd2c](https://github.com/bsorrentino/maven-confluence-plugin/commit/7749f60f448dd2c) bartolomeo sorrentino *2020-09-18 12:42:54*

**Merge branch 'feature/issue226' into develop**


[354ca265cd7e592](https://github.com/bsorrentino/maven-confluence-plugin/commit/354ca265cd7e592) bartolomeo sorrentino *2020-09-18 12:16:41*

**#226 refine unit test and integration test**


[4c1370ac366d387](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c1370ac366d387) bartolomeo sorrentino *2020-09-17 15:38:30*

**#226 fix literal escaping**


[14923c3f81e0d7d](https://github.com/bsorrentino/maven-confluence-plugin/commit/14923c3f81e0d7d) bartolomeo sorrentino *2020-09-17 15:37:51*

**#226 add integration test**


[e367144e8e866a3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e367144e8e866a3) bartolomeo sorrentino *2020-09-16 19:36:24*

**#226 add unit test for pegdown implementation**


[38a0122fab44402](https://github.com/bsorrentino/maven-confluence-plugin/commit/38a0122fab44402) bartolomeo sorrentino *2020-09-15 19:31:11*

**clean code**


[2b943e409b02ac6](https://github.com/bsorrentino/maven-confluence-plugin/commit/2b943e409b02ac6) bartolomeo sorrentino *2020-09-15 19:30:39*

**move to next developer version**


[9f822b01392899b](https://github.com/bsorrentino/maven-confluence-plugin/commit/9f822b01392899b) bartolomeo sorrentino *2020-09-15 18:38:55*

**#226 - add escaping method 'escapeMarkdownText' and add related unit tests**


[b8c49ca37c612c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8c49ca37c612c2) bartolomeo sorrentino *2020-09-15 18:35:57*

**Merge branch 'master' into develop**


[d88f9de043b9074](https://github.com/bsorrentino/maven-confluence-plugin/commit/d88f9de043b9074) bsorrentino *2020-08-05 17:19:28*

**Merge branch 'hotfix/doc'**


[3a122f806b6a870](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a122f806b6a870) bsorrentino *2020-08-05 17:19:25*

**update readme**


[25e09f337afdd24](https://github.com/bsorrentino/maven-confluence-plugin/commit/25e09f337afdd24) bsorrentino *2020-08-05 17:19:15*

**Merge branch 'master' into develop**


[f470a61e30da27c](https://github.com/bsorrentino/maven-confluence-plugin/commit/f470a61e30da27c) bsorrentino *2020-08-05 17:05:23*

**Merge branch 'hotfix/doc'**


[720ff5d8a5d5fde](https://github.com/bsorrentino/maven-confluence-plugin/commit/720ff5d8a5d5fde) bsorrentino *2020-08-05 17:05:20*

**update readme**


[0e76d6cc28896bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/0e76d6cc28896bd) bsorrentino *2020-08-05 17:05:07*

**Merge tag 'v6.9' into develop**

 * new release 6.9

[0609d36989dc41e](https://github.com/bsorrentino/maven-confluence-plugin/commit/0609d36989dc41e) bsorrentino *2020-08-05 16:58:52*


## v6.9
### Generic changes

**Merge branch 'release/6.9'**


[c490a431c4569d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/c490a431c4569d1) bsorrentino *2020-08-05 16:58:22*

**update changelog**


[cfed73664c9d47c](https://github.com/bsorrentino/maven-confluence-plugin/commit/cfed73664c9d47c) bsorrentino *2020-08-05 16:55:58*

**prepare release 6.9**


[8123a1d1d9a6d14](https://github.com/bsorrentino/maven-confluence-plugin/commit/8123a1d1d9a6d14) bsorrentino *2020-08-05 16:51:59*

**Merge branch 'feature/issue223' into develop**


[0c6543ae238c6a4](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c6543ae238c6a4) bartolomeo sorrentino *2020-08-04 15:07:04*

**issue #223 - add tests**


[81ed60d94219067](https://github.com/bsorrentino/maven-confluence-plugin/commit/81ed60d94219067) bartolomeo sorrentino *2020-08-04 15:00:33*

**issue #223 - fix scrollversions creation page flow**


[174fabf94fbe574](https://github.com/bsorrentino/maven-confluence-plugin/commit/174fabf94fbe574) bartolomeo sorrentino *2020-08-04 15:00:13*

**clean code**


[106399f7e57435f](https://github.com/bsorrentino/maven-confluence-plugin/commit/106399f7e57435f) bartolomeo sorrentino *2020-08-04 07:29:11*

**integrate log**


[c7c422736468550](https://github.com/bsorrentino/maven-confluence-plugin/commit/c7c422736468550) bsorrentino *2020-07-23 15:08:23*

**fix scrollversions implementation getting page by id**


[3b94fde85c52304](https://github.com/bsorrentino/maven-confluence-plugin/commit/3b94fde85c52304) bsorrentino *2020-07-22 16:41:33*

**update tests**


[f06388964378e85](https://github.com/bsorrentino/maven-confluence-plugin/commit/f06388964378e85) bsorrentino *2020-07-22 16:40:32*

**update readme**


[3ff242fc08ef1d3](https://github.com/bsorrentino/maven-confluence-plugin/commit/3ff242fc08ef1d3) bartolomeo sorrentino *2020-07-05 15:03:42*

**fix javadoc**


[81d5b4a3a9a447f](https://github.com/bsorrentino/maven-confluence-plugin/commit/81d5b4a3a9a447f) bartolomeo sorrentino *2020-07-05 15:01:55*

**move to developer version**


[9a20cd5666f551d](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a20cd5666f551d) bartolomeo sorrentino *2020-07-05 15:01:42*

**Merge remote-tracking branch 'origin/develop' into develop**


[c466295c7f48d60](https://github.com/bsorrentino/maven-confluence-plugin/commit/c466295c7f48d60) bartolomeo sorrentino *2020-07-05 14:58:09*

**update plugin tools version**


[923315ddb03bcf0](https://github.com/bsorrentino/maven-confluence-plugin/commit/923315ddb03bcf0) bartolomeo sorrentino *2020-07-05 14:57:59*

**Merge tag 'v6.9-rc2' into develop**

 * release version 6.9-rc2

[4dc506a5c613c2b](https://github.com/bsorrentino/maven-confluence-plugin/commit/4dc506a5c613c2b) bsorrentino *2020-07-05 14:56:27*


## v6.9-rc2
### Generic changes

**Merge branch 'release/6.9-rc2'**


[dcc060e399f1783](https://github.com/bsorrentino/maven-confluence-plugin/commit/dcc060e399f1783) bsorrentino *2020-07-05 14:55:55*

**update for new release**


[ec119a5d5009945](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec119a5d5009945) bsorrentino *2020-07-05 14:54:39*

**fix unit test**


[6b754fd2b45f948](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b754fd2b45f948) bartolomeo sorrentino *2020-07-05 13:56:43*

**Merge branch 'feature/rest_improvement' into develop**


[0995000fd0329fd](https://github.com/bsorrentino/maven-confluence-plugin/commit/0995000fd0329fd) bartolomeo sorrentino *2020-07-05 13:50:43*

**update test**


[07363c26ab17e21](https://github.com/bsorrentino/maven-confluence-plugin/commit/07363c26ab17e21) bartolomeo sorrentino *2020-07-05 13:49:54*

**add content to createPage method**


[fa953f4bcea7485](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa953f4bcea7485) bartolomeo sorrentino *2020-07-05 13:49:08*

**add skip parameter  for scrollversions addon configuration**


[3991e70e4910f85](https://github.com/bsorrentino/maven-confluence-plugin/commit/3991e70e4910f85) bartolomeo sorrentino *2020-07-05 13:47:54*

**add content to createPage method**


[35fb9497486d555](https://github.com/bsorrentino/maven-confluence-plugin/commit/35fb9497486d555) bartolomeo sorrentino *2020-07-03 19:40:33*

**add content to createPage method**


[065591fde6deebb](https://github.com/bsorrentino/maven-confluence-plugin/commit/065591fde6deebb) bartolomeo sorrentino *2020-07-03 17:19:09*

**Commonmark parser: fix break after ListBlock**


[42482138af885b8](https://github.com/bsorrentino/maven-confluence-plugin/commit/42482138af885b8) bartolomeo sorrentino *2020-06-29 22:31:54*

**Merge branch 'mr-corso-master' into develop**


[4c7d5ac790d8a34](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c7d5ac790d8a34) bartolomeo sorrentino *2020-06-29 15:49:21*

**Update markdown_processor_guide.md**

 * fix plugin example formatting

[f1c7b9b7c39f80d](https://github.com/bsorrentino/maven-confluence-plugin/commit/f1c7b9b7c39f80d) Busygin Andrey *2020-06-25 16:20:53*

**merge PR #220**


[f3d59e9e042ee9c](https://github.com/bsorrentino/maven-confluence-plugin/commit/f3d59e9e042ee9c) bartolomeo sorrentino *2020-06-21 11:30:19*

**Merge branch 'dependabot/maven/maven-confluence-core/com.fasterxml.jackson.core-jackson-databind-2.10.0.pr1' into develop**


[469ec493546eae2](https://github.com/bsorrentino/maven-confluence-plugin/commit/469ec493546eae2) bartolomeo sorrentino *2020-06-21 11:15:05*

**move to next development version**


[e38c8ab321aa08c](https://github.com/bsorrentino/maven-confluence-plugin/commit/e38c8ab321aa08c) bartolomeo sorrentino *2020-06-19 17:01:18*

**Bump jackson-databind in /maven-confluence-core**

 * Bumps [jackson-databind](https://github.com/FasterXML/jackson) from 2.9.10.4 to 2.10.0.pr1.
 * - [Release notes](https://github.com/FasterXML/jackson/releases)
 * - [Commits](https://github.com/FasterXML/jackson/commits)
 * Signed-off-by: dependabot[bot] &lt;support@github.com&gt;

[bcb35dfd3a3fdd1](https://github.com/bsorrentino/maven-confluence-plugin/commit/bcb35dfd3a3fdd1) dependabot[bot] *2020-06-19 15:31:08*

**Merge tag 'v6.9-rc1' into develop**

 * release candidate 1

[76f1473d566ced6](https://github.com/bsorrentino/maven-confluence-plugin/commit/76f1473d566ced6) bartolomeo sorrentino *2020-06-19 15:29:56*

**clean code**


[b60fdbdfb1f8907](https://github.com/bsorrentino/maven-confluence-plugin/commit/b60fdbdfb1f8907) bartolomeo sorrentino *2020-06-18 14:44:33*

**change the "deploy state manager" storage file name**


[c6e813de4cae175](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6e813de4cae175) bartolomeo sorrentino *2020-06-18 12:56:31*

**clean code**


[d83dc164ec358d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/d83dc164ec358d5) bartolomeo sorrentino *2020-06-18 12:22:40*

**clean code**


[e1f3ff759ce3c39](https://github.com/bsorrentino/maven-confluence-plugin/commit/e1f3ff759ce3c39) bartolomeo sorrentino *2020-06-16 17:40:48*

**clean javadoc**


[5cdaec707284ccb](https://github.com/bsorrentino/maven-confluence-plugin/commit/5cdaec707284ccb) bartolomeo sorrentino *2020-06-16 17:27:50*

**update javadoc**


[d14dec8672d7faa](https://github.com/bsorrentino/maven-confluence-plugin/commit/d14dec8672d7faa) bartolomeo sorrentino *2020-06-16 10:35:59*

**refactory "Mojo Parameters Management"**


[998e10987afa537](https://github.com/bsorrentino/maven-confluence-plugin/commit/998e10987afa537) bartolomeo sorrentino *2020-06-16 10:17:02*

**clean code**


[983eb1664c44eac](https://github.com/bsorrentino/maven-confluence-plugin/commit/983eb1664c44eac) bartolomeo sorrentino *2020-06-15 22:04:31*

**issue #219 - allow blog pubblication from the maven cli**


[d295ff7ce7d2d23](https://github.com/bsorrentino/maven-confluence-plugin/commit/d295ff7ce7d2d23) bartolomeo sorrentino *2020-06-15 21:48:10*

**Merge branch 'feature/issue219' into develop**


[249ce8c7ec03197](https://github.com/bsorrentino/maven-confluence-plugin/commit/249ce8c7ec03197) bartolomeo sorrentino *2020-06-15 18:00:54*

**issue #219 - finalize implementation**


[e17ff48049ec72e](https://github.com/bsorrentino/maven-confluence-plugin/commit/e17ff48049ec72e) bartolomeo sorrentino *2020-06-15 18:00:34*

**issue #219 - update markdown processor interface**


[d5d69ade231be5f](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5d69ade231be5f) bartolomeo sorrentino *2020-06-15 15:47:58*

**issue #219 - blog post implementation refinements**


[615eb488a6eb074](https://github.com/bsorrentino/maven-confluence-plugin/commit/615eb488a6eb074) bartolomeo sorrentino *2020-06-12 22:20:43*

**issue #219 - remove kotlin mojo implementation (for issues with doc gen) :-(**


[6b22ae48eb29d6c](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b22ae48eb29d6c) bartolomeo sorrentino *2020-06-12 16:16:54*

**issue #219 - fix Storage constructor**


[c1c13c222fd0e0b](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1c13c222fd0e0b) bartolomeo sorrentino *2020-06-11 21:10:35*

**issue #219 - fix XMLRPC implementation**


[21444dd3c4b54d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/21444dd3c4b54d5) bartolomeo sorrentino *2020-06-11 20:51:59*

**issue #219 - Implements Blog post in REST and XMLRPC**


[84eec6822531c06](https://github.com/bsorrentino/maven-confluence-plugin/commit/84eec6822531c06) bartolomeo sorrentino *2020-06-11 18:13:17*

**issue #219 - add Blogpost mojo, refactor Mojo hierarchy**


[7ac839d62972716](https://github.com/bsorrentino/maven-confluence-plugin/commit/7ac839d62972716) bartolomeo sorrentino *2020-06-10 14:20:49*

**Merge branch 'develop' into feature/issue219**


[76e30041bcd89ad](https://github.com/bsorrentino/maven-confluence-plugin/commit/76e30041bcd89ad) bartolomeo sorrentino *2020-06-10 07:44:19*

**enable kotlin compiler for source code**


[7d2798f3dedef50](https://github.com/bsorrentino/maven-confluence-plugin/commit/7d2798f3dedef50) bartolomeo sorrentino *2020-06-10 07:43:59*

**issue #219 - fix compiler error**


[0cd911af25105e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/0cd911af25105e6) bartolomeo sorrentino *2020-06-09 21:18:29*

**issue #219 - add sample to create blog post**


[ccd0ff1ef02dd80](https://github.com/bsorrentino/maven-confluence-plugin/commit/ccd0ff1ef02dd80) bartolomeo sorrentino *2020-06-09 21:14:21*

**clean code**


[3c7e72600033a3c](https://github.com/bsorrentino/maven-confluence-plugin/commit/3c7e72600033a3c) bartolomeo sorrentino *2020-06-09 18:58:56*

**remove deprecated getSource() method**


[651d8b9d03591fc](https://github.com/bsorrentino/maven-confluence-plugin/commit/651d8b9d03591fc) bartolomeo sorrentino *2020-06-09 12:25:53*

**clean code**


[1f5e3e0c21ffdbd](https://github.com/bsorrentino/maven-confluence-plugin/commit/1f5e3e0c21ffdbd) bartolomeo sorrentino *2020-05-27 15:02:07*

**Merge branch 'master' into develop**


[e1fd35b8951f552](https://github.com/bsorrentino/maven-confluence-plugin/commit/e1fd35b8951f552) bartolomeo sorrentino *2020-05-27 13:34:39*

**move unit test to kotlin**


[236ce3ab9e8c292](https://github.com/bsorrentino/maven-confluence-plugin/commit/236ce3ab9e8c292) bartolomeo sorrentino *2020-05-16 14:16:24*

**define a new ID class to uniform confluence ID type**


[5156acb7cd4646e](https://github.com/bsorrentino/maven-confluence-plugin/commit/5156acb7cd4646e) bartolomeo sorrentino *2020-05-16 13:56:21*

**unit test to kotlin**


[b796cc29a8b0460](https://github.com/bsorrentino/maven-confluence-plugin/commit/b796cc29a8b0460) bartolomeo sorrentino *2020-05-16 10:46:41*

**move unit test to kotlin**


[50e5702eab2b6b7](https://github.com/bsorrentino/maven-confluence-plugin/commit/50e5702eab2b6b7) bartolomeo sorrentino *2020-05-11 18:51:38*

**remove Tuple2 replaced by specific class**


[4db4fe785464f14](https://github.com/bsorrentino/maven-confluence-plugin/commit/4db4fe785464f14) bartolomeo sorrentino *2020-05-11 18:51:01*

**move unit test to kotlin**


[2706da0d61d1b87](https://github.com/bsorrentino/maven-confluence-plugin/commit/2706da0d61d1b87) bsorrentino *2020-05-09 11:39:27*

**update development version**


[1172c2288c2e2a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/1172c2288c2e2a5) bartolomeo sorrentino *2020-05-05 16:29:47*

**Merge tag 'v6.9-beta1' into develop**

 * release 6.9-beta1

[bfe191fd9d8fa7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/bfe191fd9d8fa7b) bartolomeo sorrentino *2020-05-05 16:28:42*


## v6.9-rc1
### Generic changes

**Merge branch 'release/6.9-rc1'**


[c9a80f50929c621](https://github.com/bsorrentino/maven-confluence-plugin/commit/c9a80f50929c621) bartolomeo sorrentino *2020-06-19 15:29:31*

**update changelog**


[99b747d9de0d1a0](https://github.com/bsorrentino/maven-confluence-plugin/commit/99b747d9de0d1a0) bartolomeo sorrentino *2020-06-19 15:29:07*

**update javadoc**


[9a193df5b33a77a](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a193df5b33a77a) bartolomeo sorrentino *2020-06-19 15:28:38*

**update readme**


[02b4ee720ef291b](https://github.com/bsorrentino/maven-confluence-plugin/commit/02b4ee720ef291b) bartolomeo sorrentino *2020-06-19 15:28:21*

**update site-maven-plugin version**


[d12befe23f8454d](https://github.com/bsorrentino/maven-confluence-plugin/commit/d12befe23f8454d) bartolomeo sorrentino *2020-06-19 15:28:07*

**update site documentation**


[e06c7540c024cf5](https://github.com/bsorrentino/maven-confluence-plugin/commit/e06c7540c024cf5) bartolomeo sorrentino *2020-06-18 18:30:21*

**update javadoc**


[d198ca68ddabd72](https://github.com/bsorrentino/maven-confluence-plugin/commit/d198ca68ddabd72) bartolomeo sorrentino *2020-06-18 18:30:11*

**update changelog**


[56df47542b90423](https://github.com/bsorrentino/maven-confluence-plugin/commit/56df47542b90423) bartolomeo sorrentino *2020-06-18 16:18:51*

**update changelog plugin version**


[46f0b8b3c033e64](https://github.com/bsorrentino/maven-confluence-plugin/commit/46f0b8b3c033e64) bartolomeo sorrentino *2020-06-18 16:18:29*

**move to next releaase version**


[86b63b44448ea36](https://github.com/bsorrentino/maven-confluence-plugin/commit/86b63b44448ea36) bartolomeo sorrentino *2020-06-18 16:13:44*

**move to next release version, update okhttp version**


[5056c4395960053](https://github.com/bsorrentino/maven-confluence-plugin/commit/5056c4395960053) bartolomeo sorrentino *2020-06-18 16:13:21*

**remove deprecation**


[1c494fada819e14](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c494fada819e14) bartolomeo sorrentino *2020-06-18 16:12:26*

**fix test**


[11b8ea03be319c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/11b8ea03be319c3) bartolomeo sorrentino *2020-06-18 16:12:16*

**fix test**


[0d2bfb0d625e11d](https://github.com/bsorrentino/maven-confluence-plugin/commit/0d2bfb0d625e11d) bartolomeo sorrentino *2020-06-18 16:11:38*

**remove deprecation**


[9559d4271aef456](https://github.com/bsorrentino/maven-confluence-plugin/commit/9559d4271aef456) bartolomeo sorrentino *2020-06-18 16:10:35*

**Create FUNDING.yml**


[92b8a96bf7df54b](https://github.com/bsorrentino/maven-confluence-plugin/commit/92b8a96bf7df54b) bsorrentino *2020-05-27 13:31:11*


## v6.9-beta1
### Generic changes

**Merge branch 'release/6.9-beta1'**


[e552170ee5d7f0f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e552170ee5d7f0f) bartolomeo sorrentino *2020-05-05 16:28:29*

**update documentation**


[dd6f8543209f938](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd6f8543209f938) bartolomeo sorrentino *2020-05-05 16:28:23*

**update documentation**


[4c51db04e1d499c](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c51db04e1d499c) bartolomeo sorrentino *2020-05-05 16:27:46*

**move to next version**

 * update changelog

[b7c86987054a14b](https://github.com/bsorrentino/maven-confluence-plugin/commit/b7c86987054a14b) bsorrentino *2020-05-05 09:20:43*

**refactor: update rest service implementation**


[13d0167de918fe3](https://github.com/bsorrentino/maven-confluence-plugin/commit/13d0167de918fe3) bsorrentino *2020-05-04 23:24:04*

**refactor: assume pageId as long**


[b55cd5c8fcc9628](https://github.com/bsorrentino/maven-confluence-plugin/commit/b55cd5c8fcc9628) bsorrentino *2020-05-04 13:53:36*

**rename ConfluenceService.findPageByTitle to ConfluenceService.getPageByTitle**


[675a36a4ba8c006](https://github.com/bsorrentino/maven-confluence-plugin/commit/675a36a4ba8c006) bsorrentino *2020-05-04 12:55:34*

**update development version**


[a167e13da508072](https://github.com/bsorrentino/maven-confluence-plugin/commit/a167e13da508072) bartolomeo sorrentino *2020-05-03 21:13:07*

**Merge branch 'feature/issue#217' into develop**


[5c14295fd3705ef](https://github.com/bsorrentino/maven-confluence-plugin/commit/5c14295fd3705ef) bartolomeo sorrentino *2020-05-03 18:47:47*

**clean code**


[3d85611c7432780](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d85611c7432780) bartolomeo sorrentino *2020-05-03 18:45:38*

**issue #217 - update test**


[42e1a3c2915aa77](https://github.com/bsorrentino/maven-confluence-plugin/commit/42e1a3c2915aa77) bartolomeo sorrentino *2020-05-03 18:45:21*

**issue #217 - add remove page implementation**


[2740f59c44924b0](https://github.com/bsorrentino/maven-confluence-plugin/commit/2740f59c44924b0) bartolomeo sorrentino *2020-05-03 18:44:32*

**issue #217 - update integration test**


[14ecb6d0d3d67ca](https://github.com/bsorrentino/maven-confluence-plugin/commit/14ecb6d0d3d67ca) bartolomeo sorrentino *2020-05-03 11:45:31*

**issue #217 - change ScrollVersions implementation**


[7acdad3fb5ef3f6](https://github.com/bsorrentino/maven-confluence-plugin/commit/7acdad3fb5ef3f6) bartolomeo sorrentino *2020-05-03 11:44:53*

**issue #217 - xml schema updated**


[25049f9be6e69b0](https://github.com/bsorrentino/maven-confluence-plugin/commit/25049f9be6e69b0) bartolomeo sorrentino *2020-05-03 11:43:37*

**issue #217 - xml schema updated**

 * - deprecated &#x27;parent-page&#x27; added &#x27;parentPage&#x27; instead
 * - deprecated &#x27;parent-page-id&#x27; added &#x27;parentPageId&#x27; instead

[e6bd05ea91bd7a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/e6bd05ea91bd7a5) bartolomeo sorrentino *2020-05-03 11:42:32*

**issue #217 - xml schema updated - deprecated 'ignore-variables' added 'ignoreVariables' instead**


[869d0973d9dd9d0](https://github.com/bsorrentino/maven-confluence-plugin/commit/869d0973d9dd9d0) bartolomeo sorrentino *2020-05-03 11:27:36*

**issue #217 refactor scrollversions addon implementation**


[b42494b2b9a1c78](https://github.com/bsorrentino/maven-confluence-plugin/commit/b42494b2b9a1c78) bartolomeo sorrentino *2020-05-02 08:07:30*

**continue to move toward CompletableFuture**


[a653c7da6171172](https://github.com/bsorrentino/maven-confluence-plugin/commit/a653c7da6171172) bartolomeo sorrentino *2020-04-30 08:38:31*

**start creating a new scrollverions implememntation**


[cba3a0d81d7249d](https://github.com/bsorrentino/maven-confluence-plugin/commit/cba3a0d81d7249d) bsorrentino *2020-04-29 22:25:21*

**improve loadParentPage() integration**


[82bbc2333e00dc1](https://github.com/bsorrentino/maven-confluence-plugin/commit/82bbc2333e00dc1) bsorrentino *2020-04-29 15:51:29*

**update tests**


[f342738f1e5e1c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/f342738f1e5e1c1) bartolomeo sorrentino *2020-04-28 22:18:57*

**methods compilance with CompletableFuture**


[76e7bff7bd9df97](https://github.com/bsorrentino/maven-confluence-plugin/commit/76e7bff7bd9df97) bartolomeo sorrentino *2020-04-28 22:18:44*

**all methods compliant with CompletableFuture**


[5454c8f0a2ac014](https://github.com/bsorrentino/maven-confluence-plugin/commit/5454c8f0a2ac014) bartolomeo sorrentino *2020-04-28 22:17:29*

**supplyed async on attachment management**


[9ab5b5173483b84](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ab5b5173483b84) bartolomeo sorrentino *2020-04-28 11:14:49*

**clean code**


[aa2d803694c7d0d](https://github.com/bsorrentino/maven-confluence-plugin/commit/aa2d803694c7d0d) bartolomeo sorrentino *2020-04-28 10:57:23*

**bulk add labels using completable future**


[15677e774d21876](https://github.com/bsorrentino/maven-confluence-plugin/commit/15677e774d21876) bartolomeo sorrentino *2020-04-28 10:52:12*

**move to next developer version**


[c2c27f0b6da500a](https://github.com/bsorrentino/maven-confluence-plugin/commit/c2c27f0b6da500a) bartolomeo sorrentino *2020-04-28 07:12:57*

**test new strategy for scrollversions pubblication**


[c01829264a37ac4](https://github.com/bsorrentino/maven-confluence-plugin/commit/c01829264a37ac4) bartolomeo sorrentino *2020-04-28 07:01:56*

**clean code**


[bef77f72c8a7966](https://github.com/bsorrentino/maven-confluence-plugin/commit/bef77f72c8a7966) bartolomeo sorrentino *2020-04-27 14:05:38*

**move SiteTest to kotlin**


[f4b28b9d06b6d4f](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4b28b9d06b6d4f) bartolomeo sorrentino *2020-04-27 14:04:33*

**Merge branch 'master' into develop**


[3813bcf7d378f6a](https://github.com/bsorrentino/maven-confluence-plugin/commit/3813bcf7d378f6a) bartolomeo sorrentino *2020-04-27 10:53:06*

**Merge branch 'hotfix/doc'**


[ea0d2b6d57978e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/ea0d2b6d57978e5) bartolomeo sorrentino *2020-04-27 10:53:02*

**update readme**


[effaf9b82a77be5](https://github.com/bsorrentino/maven-confluence-plugin/commit/effaf9b82a77be5) bartolomeo sorrentino *2020-04-27 10:52:53*

**clean code**


[ddd0459f723389c](https://github.com/bsorrentino/maven-confluence-plugin/commit/ddd0459f723389c) bartolomeo sorrentino *2020-04-27 10:36:28*

**update javadoc**


[3bd03d67bdfa37b](https://github.com/bsorrentino/maven-confluence-plugin/commit/3bd03d67bdfa37b) bsorrentino *2020-04-26 18:23:38*

**fix confluence encode of <br> tag**


[f5ac9de43fe221d](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5ac9de43fe221d) bsorrentino *2020-04-26 18:22:55*

**move to next development version**


[2fd87f8ce6581ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/2fd87f8ce6581ed) bsorrentino *2020-04-26 18:16:48*

**Merge tag 'v6.8' into develop**

 * release 6.8 - markdown processor support

[5c65964fccc4a84](https://github.com/bsorrentino/maven-confluence-plugin/commit/5c65964fccc4a84) bsorrentino *2020-04-26 10:01:39*


## v6.8
### Generic changes

**Merge branch 'release/6.8'**


[3008d948a307e89](https://github.com/bsorrentino/maven-confluence-plugin/commit/3008d948a307e89) bsorrentino *2020-04-26 10:01:15*

**upgrade version, update documentation and changelog**


[87c262cf37460f1](https://github.com/bsorrentino/maven-confluence-plugin/commit/87c262cf37460f1) bsorrentino *2020-04-26 09:59:56*

**fix log message on Site deletion**


[4e2ec2204b1b7a1](https://github.com/bsorrentino/maven-confluence-plugin/commit/4e2ec2204b1b7a1) bartolomeo sorrentino *2020-04-25 17:49:35*

**Merge branch 'feature/issue#215' into develop**


[17f6a00b52512e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/17f6a00b52512e5) bartolomeo sorrentino *2020-04-25 17:42:39*

**issue #215 #212 update unit tests**


[b205384c7aeec6e](https://github.com/bsorrentino/maven-confluence-plugin/commit/b205384c7aeec6e) bartolomeo sorrentino *2020-04-25 17:40:03*

**issue #215 #212 update flexmark implementation**


[a34b5d743101165](https://github.com/bsorrentino/maven-confluence-plugin/commit/a34b5d743101165) bartolomeo sorrentino *2020-04-25 17:39:26*

**issue #215 #212 update pegdown implementation**


[899496b745a7eb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/899496b745a7eb2) bartolomeo sorrentino *2020-04-25 17:38:58*

**issue #215 #212 update the commonmark implementation**


[98106ce4b2fdd57](https://github.com/bsorrentino/maven-confluence-plugin/commit/98106ce4b2fdd57) bartolomeo sorrentino *2020-04-25 17:38:26*

**issue #215 #212 add pagePrefixToApply as optional, add page in MarkdownParserContext**


[063d7e68ed2fab0](https://github.com/bsorrentino/maven-confluence-plugin/commit/063d7e68ed2fab0) bartolomeo sorrentino *2020-04-25 17:36:57*

**issue #215 #212 add processLinkUrl helper**


[124cd0062112ebf](https://github.com/bsorrentino/maven-confluence-plugin/commit/124cd0062112ebf) bartolomeo sorrentino *2020-04-25 17:32:06*

**issue #215 add pagePrefixToApply as optional**


[a95ebdd5b35879c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a95ebdd5b35879c) bartolomeo sorrentino *2020-04-25 17:31:02*

**update markdown doc**


[08bca6adec04aa9](https://github.com/bsorrentino/maven-confluence-plugin/commit/08bca6adec04aa9) bartolomeo sorrentino *2020-04-25 17:28:33*

**issue #215 #212 add integration test**


[707fb0a1f256e97](https://github.com/bsorrentino/maven-confluence-plugin/commit/707fb0a1f256e97) bartolomeo sorrentino *2020-04-25 17:27:47*

**clean code**


[73d0931ed13fbde](https://github.com/bsorrentino/maven-confluence-plugin/commit/73d0931ed13fbde) bartolomeo sorrentino *2020-04-24 18:46:55*

**charset refinements**


[96d93947845019f](https://github.com/bsorrentino/maven-confluence-plugin/commit/96d93947845019f) bartolomeo sorrentino *2020-04-24 18:08:01*

**issue #212**

 * add test stuff

[0ac7916ca147756](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ac7916ca147756) bartolomeo sorrentino *2020-04-24 18:04:57*

**Merge branch 'feature/issue#154' into develop**


[7610cd2dbe9e515](https://github.com/bsorrentino/maven-confluence-plugin/commit/7610cd2dbe9e515) bartolomeo sorrentino *2020-04-24 14:33:46*

**merge pom from develop**


[25d2245136080c6](https://github.com/bsorrentino/maven-confluence-plugin/commit/25d2245136080c6) bartolomeo sorrentino *2020-04-24 13:17:30*

**Merge branch 'develop' into feature/issue#154**


[65439baea3ec8bf](https://github.com/bsorrentino/maven-confluence-plugin/commit/65439baea3ec8bf) bartolomeo sorrentino *2020-04-24 13:17:00*

**issue #154**

 * update documentation
 * improve test over images

[6be66a95b634338](https://github.com/bsorrentino/maven-confluence-plugin/commit/6be66a95b634338) bartolomeo sorrentino *2020-04-24 11:33:31*

**issue #154 - fix image conversion using macro**


[e4dcb6ca95278d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/e4dcb6ca95278d8) bsorrentino *2020-04-24 00:09:22*

**Merge tag 'v6.7.3' into develop**

 * new minor release

[5040651dc0e2fc3](https://github.com/bsorrentino/maven-confluence-plugin/commit/5040651dc0e2fc3) bsorrentino *2020-04-23 22:08:48*

**issue #154 - update documentation**


[c092becb31e7240](https://github.com/bsorrentino/maven-confluence-plugin/commit/c092becb31e7240) bartolomeo sorrentino *2020-04-23 20:25:53*

**issue #154 - add markdown test**


[528d098f7f56b72](https://github.com/bsorrentino/maven-confluence-plugin/commit/528d098f7f56b72) bartolomeo sorrentino *2020-04-23 18:26:36*

**move plugin-test project in the root directory**


[1b66eb08486a13f](https://github.com/bsorrentino/maven-confluence-plugin/commit/1b66eb08486a13f) bartolomeo sorrentino *2020-04-23 14:47:10*

**move plugin-test project in the root directory**


[4d2ab56bc8f8cca](https://github.com/bsorrentino/maven-confluence-plugin/commit/4d2ab56bc8f8cca) bartolomeo sorrentino *2020-04-23 14:46:34*

**refactor package layout**


[400a0ecfca757fa](https://github.com/bsorrentino/maven-confluence-plugin/commit/400a0ecfca757fa) bartolomeo sorrentino *2020-04-23 14:42:37*

**issue #154 - remove deprecated method**


[60b040f35a8dd7f](https://github.com/bsorrentino/maven-confluence-plugin/commit/60b040f35a8dd7f) bartolomeo sorrentino *2020-04-23 14:29:06*

**issue #154 - remove deprecated method**


[0fdd587deab1954](https://github.com/bsorrentino/maven-confluence-plugin/commit/0fdd587deab1954) bartolomeo sorrentino *2020-04-23 14:28:30*

**issue #154 - remove deprecated method**


[d1d33b07b8d76e3](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1d33b07b8d76e3) bartolomeo sorrentino *2020-04-23 14:27:32*

**issue #154 - add MarkdownProcessorInfo to the plugin configuration**


[8ad6c62f7899647](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ad6c62f7899647) bartolomeo sorrentino *2020-04-22 21:48:48*

**package refactor**


[f02491ab7a602d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/f02491ab7a602d1) bartolomeo sorrentino *2020-04-22 16:24:01*

**issue #154 - add support of "Markdown Processor Provider"**


[e81706476cdcf3a](https://github.com/bsorrentino/maven-confluence-plugin/commit/e81706476cdcf3a) bartolomeo sorrentino *2020-04-22 16:15:31*

**issue #154 - clean code**


[d19536d7b458f02](https://github.com/bsorrentino/maven-confluence-plugin/commit/d19536d7b458f02) bartolomeo sorrentino *2020-04-22 11:08:26*

**issue #154 - add common interface MarkdownParserContext**


[6f7802cecfdd5f2](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f7802cecfdd5f2) bartolomeo sorrentino *2020-04-21 22:08:03*

**issue #154 - update documentation**


[c7da7a8fce372dc](https://github.com/bsorrentino/maven-confluence-plugin/commit/c7da7a8fce372dc) bartolomeo sorrentino *2020-04-21 20:39:30*

**issue #154 - add commonmark extension to support Notice Block syntax**


[8ca2613ca6337a3](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ca2613ca6337a3) bartolomeo sorrentino *2020-04-21 20:34:42*

**issue #154 clean code**


[533e919bf35a8f1](https://github.com/bsorrentino/maven-confluence-plugin/commit/533e919bf35a8f1) bartolomeo sorrentino *2020-04-20 21:51:54*

**update kotlin configuration**


[c5d99c43791d658](https://github.com/bsorrentino/maven-confluence-plugin/commit/c5d99c43791d658) bartolomeo sorrentino *2020-04-20 21:35:45*

**move unit test to kotlin**


[81f2334c537967f](https://github.com/bsorrentino/maven-confluence-plugin/commit/81f2334c537967f) bartolomeo sorrentino *2020-04-20 21:10:56*

**issue #154 - add commonmark tests for tables, code, horizontalrule**


[13e21f2dba2aa9d](https://github.com/bsorrentino/maven-confluence-plugin/commit/13e21f2dba2aa9d) bartolomeo sorrentino *2020-04-10 21:32:12*

**issue #154 - commonmark add support for: images, blockquote, links**


[4ed78b8dca6a4ee](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ed78b8dca6a4ee) bartolomeo sorrentino *2020-04-08 23:47:21*

**issue #154 - commonmark add support for: headers, emphasis and lists**


[0ec82ef7a9c4c19](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ec82ef7a9c4c19) bartolomeo sorrentino *2020-04-07 22:08:44*

**issue #154 - update commonmark implememntation**


[25d5428def56910](https://github.com/bsorrentino/maven-confluence-plugin/commit/25d5428def56910) bartolomeo sorrentino *2020-03-23 23:57:26*

**issue #154 - start implementation of markdown processor  using either flexmark and commonmark**


[715ccf861725349](https://github.com/bsorrentino/maven-confluence-plugin/commit/715ccf861725349) bartolomeo sorrentino *2020-03-21 16:36:20*

**rename project**


[a497b04a907fdad](https://github.com/bsorrentino/maven-confluence-plugin/commit/a497b04a907fdad) bartolomeo sorrentino *2020-03-21 12:13:09*

**issue #154 - move test resources in markdown processor module**


[634e011fb105745](https://github.com/bsorrentino/maven-confluence-plugin/commit/634e011fb105745) bartolomeo sorrentino *2020-03-21 12:03:23*

**issue #154 - create a module with markdown processor implementation**


[f747fa8f5b65c71](https://github.com/bsorrentino/maven-confluence-plugin/commit/f747fa8f5b65c71) bartolomeo sorrentino *2020-03-21 12:02:22*


## v6.7.3
### Generic changes

**Merge branch 'release/6.7.3'**


[8f6e889257b0ffe](https://github.com/bsorrentino/maven-confluence-plugin/commit/8f6e889257b0ffe) bsorrentino *2020-04-23 22:08:34*

**move to next release**


[75811b79bbc8bfa](https://github.com/bsorrentino/maven-confluence-plugin/commit/75811b79bbc8bfa) bsorrentino *2020-04-23 21:58:50*

**fix security issue PR #214**


[5be4a8e69c81e92](https://github.com/bsorrentino/maven-confluence-plugin/commit/5be4a8e69c81e92) bsorrentino *2020-04-23 21:43:46*

**Merge branch 'feature/issue#213' into develop**


[80918006203e287](https://github.com/bsorrentino/maven-confluence-plugin/commit/80918006203e287) bsorrentino *2020-04-23 21:41:39*

**move to next develop version**


[e973e49e077ef63](https://github.com/bsorrentino/maven-confluence-plugin/commit/e973e49e077ef63) bsorrentino *2020-04-23 21:40:20*

**issue #213 - move from int to long**


[5a4b965da3fbfdb](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a4b965da3fbfdb) bsorrentino *2020-04-23 21:39:50*

**update changelog**


[0dd3fe25a0a777d](https://github.com/bsorrentino/maven-confluence-plugin/commit/0dd3fe25a0a777d) bsorrentino *2020-03-11 20:15:50*

**Merge tag 'v6.7.2' into develop**

 * release 6.7.2

[b265c006c6917c4](https://github.com/bsorrentino/maven-confluence-plugin/commit/b265c006c6917c4) bsorrentino *2020-03-11 19:16:17*

**fix security #211**


[ddf9bcb0530c00b](https://github.com/bsorrentino/maven-confluence-plugin/commit/ddf9bcb0530c00b) bsorrentino *2020-03-11 15:54:25*

**add new module 'scrollversions-addon' to separate such implementation**

 * rename directory from &#x27;maven-confluence-processor-freemaker&#x27; to &#x27;site-processor-freemaker&#x27;

[600aa442c5bab26](https://github.com/bsorrentino/maven-confluence-plugin/commit/600aa442c5bab26) bsorrentino *2020-03-11 15:32:13*

**update maven-compiler-plugin version : from 3.7.0 -> 3.8.1**


[214a935b9d5cfa0](https://github.com/bsorrentino/maven-confluence-plugin/commit/214a935b9d5cfa0) bsorrentino *2020-02-10 16:16:32*

**Merge branch 'release/6.7.1' into develop**


[3b844225b0b55d9](https://github.com/bsorrentino/maven-confluence-plugin/commit/3b844225b0b55d9) bsorrentino *2019-11-13 19:17:08*

**Merge branch 'feature/PR#210' into develop**


[954ee385cd5fa83](https://github.com/bsorrentino/maven-confluence-plugin/commit/954ee385cd5fa83) bsorrentino *2019-11-13 11:27:51*

**update a next development version**


[832f0ee2bc689e9](https://github.com/bsorrentino/maven-confluence-plugin/commit/832f0ee2bc689e9) bsorrentino *2019-11-13 11:27:47*

**ignore unknown json properties**


[189d6592c8c0bf2](https://github.com/bsorrentino/maven-confluence-plugin/commit/189d6592c8c0bf2) Nicola Lagnena *2019-11-12 13:04:06*

**update doc**


[f30bb09ad50788a](https://github.com/bsorrentino/maven-confluence-plugin/commit/f30bb09ad50788a) bartolomeo sorrentino *2019-11-02 20:43:16*

**Merge branch 'release/6.7' into develop**


[d5b29dca5d705b2](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5b29dca5d705b2) bartolomeo sorrentino *2019-11-02 20:23:19*

**Merge branch 'feature/issue#202' into develop**


[fdadcd49b8b531a](https://github.com/bsorrentino/maven-confluence-plugin/commit/fdadcd49b8b531a) bartolomeo sorrentino *2019-11-02 19:22:48*

**issue #202**

 * add site processor usage documentation

[4bbc7baf85a37b3](https://github.com/bsorrentino/maven-confluence-plugin/commit/4bbc7baf85a37b3) bartolomeo sorrentino *2019-11-02 19:22:19*

**move to next developer version**


[cd3d38378010f33](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd3d38378010f33) bartolomeo sorrentino *2019-11-02 19:21:48*

**upgrade maven-compiler-plugin**


[222e158df5f5dd1](https://github.com/bsorrentino/maven-confluence-plugin/commit/222e158df5f5dd1) bartolomeo sorrentino *2019-11-02 17:36:06*

**issue #202**

 * add an ah-hoc user drive test

[0fba74cdcdc0d2e](https://github.com/bsorrentino/maven-confluence-plugin/commit/0fba74cdcdc0d2e) bartolomeo sorrentino *2019-11-02 11:26:48*

**issue #202**

 * rename from PreprocessorService to SiteProcessorService
 * rename preprocess() method to process()

[0b6407b40e0bbf6](https://github.com/bsorrentino/maven-confluence-plugin/commit/0b6407b40e0bbf6) bartolomeo sorrentino *2019-11-02 11:26:02*

**issue #202**

 * rename freemaker processor module

[155f8fbf08dd6af](https://github.com/bsorrentino/maven-confluence-plugin/commit/155f8fbf08dd6af) bartolomeo sorrentino *2019-11-02 11:22:11*

**issue #202**

 * rename freemaker processor module

[814d4151ee19652](https://github.com/bsorrentino/maven-confluence-plugin/commit/814d4151ee19652) bartolomeo sorrentino *2019-11-02 11:21:22*

**align commons logging default level to maven logging level**


[5a38a327d26c5d0](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a38a327d26c5d0) bartolomeo sorrentino *2019-11-02 11:19:16*

**configure commons logging to SimpleLog**


[850235ed1ab764d](https://github.com/bsorrentino/maven-confluence-plugin/commit/850235ed1ab764d) bartolomeo sorrentino *2019-11-02 11:16:24*

**fix security vulnerability #com.fasterxml.jackson.core:jackson-databind**


[8db6f3176abb4d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/8db6f3176abb4d1) bartolomeo sorrentino *2019-11-02 10:10:33*

**issue #202**

 * rename Preprocessor to PreprocessorService

[f79e6fd35e7854e](https://github.com/bsorrentino/maven-confluence-plugin/commit/f79e6fd35e7854e) bartolomeo sorrentino *2019-11-02 08:09:50*

**issue #202**

 * create a new module maven-confluence-siteprocessor-freemarker and moved the freemaker implementation and unit test there
 * use java service provider interface to load the markup engine so we can support more preprocessor engine

[e766c4d70d6cdb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/e766c4d70d6cdb2) bsorrentino *2019-11-01 22:07:16*

**remove useless stuff**


[a7a22467bcefa8c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a7a22467bcefa8c) bsorrentino *2019-11-01 20:36:27*

**Merge branch 'feature/GH-202_add-markup-to-site-definitions' of https://github.com/LZaruba/maven-confluence-plugin into develop**


[a4bd55f02ddd158](https://github.com/bsorrentino/maven-confluence-plugin/commit/a4bd55f02ddd158) bsorrentino *2019-11-01 20:15:47*

**move to next developer version**


[c30d4f25a0863d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/c30d4f25a0863d4) bsorrentino *2019-11-01 20:15:08*

**Merge branch 'feature/fix-integration-tests' into develop**


[63d710292d7d600](https://github.com/bsorrentino/maven-confluence-plugin/commit/63d710292d7d600) bsorrentino *2019-10-31 16:34:46*

**Merge branch 'bugfix/fix-integration-tests' of https://github.com/neko-gg/maven-confluence-plugin into neko-gg-bugfix/fix-integration-tests**


[886af1e65605e78](https://github.com/bsorrentino/maven-confluence-plugin/commit/886af1e65605e78) bsorrentino *2019-10-31 16:32:35*

**GH-202 Add possibility to use markup in the site definition**

 * organized imports after the merge of master branch

[0ef5166558b32ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ef5166558b32ec) Lukas Zaruba *2019-10-31 09:43:37*

**Merge branch 'master' into feature/GH-202_add-markup-to-site-definitions**

 * # Conflicts:
 * #	maven-confluence-core/src/test/java/org/bsc/confluence/model/SiteTest.java
 * #	maven-confluence-reporting-plugin/src/main/java/org/bsc/maven/confluence/plugin/ConfluenceDeployMojo.java

[c0791fb57d3ce37](https://github.com/bsorrentino/maven-confluence-plugin/commit/c0791fb57d3ce37) Lukas Zaruba *2019-10-31 09:41:06*

**Merge branch 'master' into develop**


[cb9841c78816a5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/cb9841c78816a5b) bsorrentino *2019-10-30 23:04:22*

**Merge branch 'release/6.6' into develop**


[637af64ea7ff06e](https://github.com/bsorrentino/maven-confluence-plugin/commit/637af64ea7ff06e) bsorrentino *2019-10-30 22:47:31*

**GH-202 Add possibility to use markup in the site definition**

 * Added freemarker capability to the site definition, tests, documentation

[952c03196ca6a8d](https://github.com/bsorrentino/maven-confluence-plugin/commit/952c03196ca6a8d) Lukas Zaruba *2019-10-20 16:30:45*

**use hashtable as map implementation, change illegal character in file path, add children pages to webserver mock**


[de3174dce71f7a4](https://github.com/bsorrentino/maven-confluence-plugin/commit/de3174dce71f7a4) Nicola Lagnena *2019-10-05 12:03:17*

**use mutable list**


[ea290121703174d](https://github.com/bsorrentino/maven-confluence-plugin/commit/ea290121703174d) Nicola Lagnena *2019-10-05 08:27:47*

**Merge branch 'feature/#195-fix-junit-tests-on-windows' into develop**


[ad77cb5374a504d](https://github.com/bsorrentino/maven-confluence-plugin/commit/ad77cb5374a504d) bartolomeo sorrentino *2019-09-29 15:02:24*

**fix issue #195**


[b55e70da0ee4fd5](https://github.com/bsorrentino/maven-confluence-plugin/commit/b55e70da0ee4fd5) Nicola Lagnena *2019-09-28 10:44:29*

**Merge branch 'release/6.4' into develop**


[bbccfc20fd0d65f](https://github.com/bsorrentino/maven-confluence-plugin/commit/bbccfc20fd0d65f) bsorrentino *2019-08-02 16:15:50*

**update doc**


[c79afa3f3d845dc](https://github.com/bsorrentino/maven-confluence-plugin/commit/c79afa3f3d845dc) bsorrentino *2019-08-02 16:14:38*

**update version to next release**


[ccb00a101a70b17](https://github.com/bsorrentino/maven-confluence-plugin/commit/ccb00a101a70b17) bsorrentino *2019-08-02 15:59:24*

**update changelog**


[06cd6a2ea7b179c](https://github.com/bsorrentino/maven-confluence-plugin/commit/06cd6a2ea7b179c) bsorrentino *2019-08-02 15:57:46*

**update changelog**


[38411400703c8e3](https://github.com/bsorrentino/maven-confluence-plugin/commit/38411400703c8e3) bsorrentino *2019-08-02 15:56:27*

**Merge branch 'feature/issue#188' into develop**


[1999c2b1348c28a](https://github.com/bsorrentino/maven-confluence-plugin/commit/1999c2b1348c28a) bsorrentino *2019-08-02 15:42:53*

**issue #188 - clean code**


[41afb70a1caa81d](https://github.com/bsorrentino/maven-confluence-plugin/commit/41afb70a1caa81d) bsorrentino *2019-08-02 08:59:05*

**clean code**


[ee14d2879b6c3d3](https://github.com/bsorrentino/maven-confluence-plugin/commit/ee14d2879b6c3d3) bsorrentino *2019-08-02 08:53:14*

**issue #188 - use site descriptor for identify start page for deletion**


[98d3a92c03a12b8](https://github.com/bsorrentino/maven-confluence-plugin/commit/98d3a92c03a12b8) bsorrentino *2019-08-02 08:52:46*

**clean code**


[a25bd78e645021f](https://github.com/bsorrentino/maven-confluence-plugin/commit/a25bd78e645021f) bsorrentino *2019-08-01 23:02:40*

**issue #188 - add support of site descriptor to delete mojo**


[f4419e3daf6db1e](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4419e3daf6db1e) bsorrentino *2019-08-01 15:31:42*

**issue #188 - add support of site descriptor to delete mojo**


[f325421c47160a4](https://github.com/bsorrentino/maven-confluence-plugin/commit/f325421c47160a4) bsorrentino *2019-08-01 15:30:35*

**Merge branch 'feature/issue#193' into develop**


[b4708e76a8f61b4](https://github.com/bsorrentino/maven-confluence-plugin/commit/b4708e76a8f61b4) bsorrentino *2019-08-01 14:39:31*

**move to next developer version**


[886d4f116648c7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/886d4f116648c7e) bsorrentino *2019-07-31 22:19:23*

**issue #193 add unit test**


[aca906c5b3b78c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/aca906c5b3b78c5) bsorrentino *2019-07-31 21:56:35*

**clean code**


[7025aad615755ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/7025aad615755ec) bsorrentino *2019-07-31 21:56:17*

**issue #193 handle relative page link in visit(ExpLinkNode)**


[9653ef7c16bf63b](https://github.com/bsorrentino/maven-confluence-plugin/commit/9653ef7c16bf63b) bsorrentino *2019-07-31 21:55:57*

**issue #193 add getRelativeUri() method**


[a9c3d2fd2216a4c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a9c3d2fd2216a4c) bsorrentino *2019-07-31 21:53:44*

**clean code**


[0af6bd6ed081614](https://github.com/bsorrentino/maven-confluence-plugin/commit/0af6bd6ed081614) bsorrentino *2019-07-31 21:39:04*

**Merge branch 'feature/issue#192' into develop**


[5eb1933dfb5ba00](https://github.com/bsorrentino/maven-confluence-plugin/commit/5eb1933dfb5ba00) bsorrentino *2019-07-31 14:51:14*

**issue #193**

 * prepare unit test

[894c9c2bd80324e](https://github.com/bsorrentino/maven-confluence-plugin/commit/894c9c2bd80324e) bsorrentino *2019-07-31 04:41:21*

**issue #193**

 * move site from property to supplier method

[ea1a531725f128d](https://github.com/bsorrentino/maven-confluence-plugin/commit/ea1a531725f128d) bsorrentino *2019-07-31 04:40:58*

**issue #193 - move site from property to supplier methos**


[4af2696c38f12c0](https://github.com/bsorrentino/maven-confluence-plugin/commit/4af2696c38f12c0) bsorrentino *2019-07-31 04:38:52*

**fix - stop ignoring unit test**


[75d3d462e7ce815](https://github.com/bsorrentino/maven-confluence-plugin/commit/75d3d462e7ce815) bsorrentino *2019-07-31 04:37:57*

**clean code**


[ed62181b39e3f8b](https://github.com/bsorrentino/maven-confluence-plugin/commit/ed62181b39e3f8b) bsorrentino *2019-07-31 04:37:14*

**issue #193 - add site model to ToConfluenceProcessor**


[5766b99a0833c01](https://github.com/bsorrentino/maven-confluence-plugin/commit/5766b99a0833c01) bsorrentino *2019-07-31 03:22:34*

**issue #192 - improve generate attachment logging**


[f49ffd2984a3fa0](https://github.com/bsorrentino/maven-confluence-plugin/commit/f49ffd2984a3fa0) bartolomeo sorrentino *2019-07-26 13:14:23*

**update ignore**


[24e0e0c0a1ec3f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/24e0e0c0a1ec3f3) bartolomeo sorrentino *2019-07-26 13:13:48*

**move to next developer version**


[f1a008cb77aed41](https://github.com/bsorrentino/maven-confluence-plugin/commit/f1a008cb77aed41) bartolomeo sorrentino *2019-07-26 13:09:53*

**remove deprecated method**


[9e029b782ace0c9](https://github.com/bsorrentino/maven-confluence-plugin/commit/9e029b782ace0c9) bsorrentino *2019-07-17 13:25:38*

**add lombok support**


[c0972df6937b53f](https://github.com/bsorrentino/maven-confluence-plugin/commit/c0972df6937b53f) bsorrentino *2019-07-17 13:18:56*

**Merge branch 'hotfix/docs' into develop**


[7382c8e4ccaa00e](https://github.com/bsorrentino/maven-confluence-plugin/commit/7382c8e4ccaa00e) bsorrentino *2019-07-16 14:15:56*


## v6.7.2
### Generic changes

**Merge branch 'release/6.7.2'**


[18d4f07084856ba](https://github.com/bsorrentino/maven-confluence-plugin/commit/18d4f07084856ba) bsorrentino *2020-03-11 19:16:02*

**update readme**


[a20187192b02fca](https://github.com/bsorrentino/maven-confluence-plugin/commit/a20187192b02fca) bsorrentino *2020-03-11 19:15:34*

**update changelog**


[cfa22033afe5523](https://github.com/bsorrentino/maven-confluence-plugin/commit/cfa22033afe5523) bsorrentino *2020-03-11 19:15:21*

**move to release version**


[3f6c63eb16a1ac0](https://github.com/bsorrentino/maven-confluence-plugin/commit/3f6c63eb16a1ac0) bsorrentino *2020-03-11 19:15:01*


## v6.7.1
### Generic changes

**Merge branch 'release/6.7.1'**


[511debfee64cce2](https://github.com/bsorrentino/maven-confluence-plugin/commit/511debfee64cce2) bsorrentino *2019-11-13 19:16:39*

**update readme**


[c3185c26259a935](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3185c26259a935) bsorrentino *2019-11-13 14:40:09*

**update changelog**


[6cc531f33009388](https://github.com/bsorrentino/maven-confluence-plugin/commit/6cc531f33009388) bsorrentino *2019-11-13 14:38:26*

**set release version**


[4c3cbb7a0380d40](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c3cbb7a0380d40) bsorrentino *2019-11-13 14:37:09*


## v6.7
### Generic changes

**Merge branch 'release/6.7'**


[a973d939e3d2e82](https://github.com/bsorrentino/maven-confluence-plugin/commit/a973d939e3d2e82) bartolomeo sorrentino *2019-11-02 20:21:35*

**update readme**


[e2d76eb0efbf76b](https://github.com/bsorrentino/maven-confluence-plugin/commit/e2d76eb0efbf76b) bartolomeo sorrentino *2019-11-02 20:21:05*

**update changelog**


[2179af1be5850b0](https://github.com/bsorrentino/maven-confluence-plugin/commit/2179af1be5850b0) bartolomeo sorrentino *2019-11-02 20:07:32*

**set release version**


[8c4fc4ba9ebf077](https://github.com/bsorrentino/maven-confluence-plugin/commit/8c4fc4ba9ebf077) bartolomeo sorrentino *2019-11-02 19:49:54*

**update changelog**


[a0d8dfc75d5d5f4](https://github.com/bsorrentino/maven-confluence-plugin/commit/a0d8dfc75d5d5f4) bsorrentino *2019-10-30 23:03:54*


## v6.6
### Generic changes

**Merge branch 'release/6.6'**


[518a3edbd70070a](https://github.com/bsorrentino/maven-confluence-plugin/commit/518a3edbd70070a) bsorrentino *2019-10-30 22:47:05*

**update readme**


[28ed787d628e487](https://github.com/bsorrentino/maven-confluence-plugin/commit/28ed787d628e487) bsorrentino *2019-10-30 22:43:03*

**set release version**


[6345fab6bbb571c](https://github.com/bsorrentino/maven-confluence-plugin/commit/6345fab6bbb571c) bsorrentino *2019-10-30 22:39:29*

**update changelog**


[7d2dcf2504a96fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/7d2dcf2504a96fb) bsorrentino *2019-10-30 22:38:53*

**Merge branch 'feature/GH-206_Add-possibility-to-disable-url-properties-resolving' into develop**


[b0112cba3dcec87](https://github.com/bsorrentino/maven-confluence-plugin/commit/b0112cba3dcec87) bsorrentino *2019-10-30 22:27:19*

**update javadoc version of processProperties parameter**


[f7b7e852f4b0c73](https://github.com/bsorrentino/maven-confluence-plugin/commit/f7b7e852f4b0c73) bsorrentino *2019-10-30 22:22:18*

**Merge branch 'develop' into feature/GH-206_Add-possibility-to-disable-url-properties-resolving**


[7edfae82e75309e](https://github.com/bsorrentino/maven-confluence-plugin/commit/7edfae82e75309e) bsorrentino *2019-10-30 22:13:52*

**Merge branch 'feature/GH-206_Add-possibility-to-disable-url-properties-resolving' of https://github.com/LZaruba/maven-confluence-plugin into LZaruba-feature/GH-206_Add-possibility-to-disable-url-properties-resolving**


[e25c85ef55e6422](https://github.com/bsorrentino/maven-confluence-plugin/commit/e25c85ef55e6422) bsorrentino *2019-10-30 22:12:55*

**Merge branch 'feature/GH-204_Replace-invalid-characters-in-labels' into develop**


[f5cbd46c0f43be4](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5cbd46c0f43be4) bsorrentino *2019-10-30 22:02:23*

**Merge branch 'develop' into feature/GH-204_Replace-invalid-characters-in-labels**


[3f8096e717fc7f1](https://github.com/bsorrentino/maven-confluence-plugin/commit/3f8096e717fc7f1) bsorrentino *2019-10-30 21:56:47*

**Merge branch 'feature/GH-204_Replace-invalid-characters-in-labels' of https://github.com/LZaruba/maven-confluence-plugin into LZaruba-feature/GH-204_Replace-invalid-characters-in-labels**


[e239f122470b5aa](https://github.com/bsorrentino/maven-confluence-plugin/commit/e239f122470b5aa) bsorrentino *2019-10-30 21:48:07*

**move to next developer version**


[3e58683b704fab5](https://github.com/bsorrentino/maven-confluence-plugin/commit/3e58683b704fab5) bsorrentino *2019-10-30 21:42:36*

**Merge branch 'feature/GH-200_add-locale-configuration' into develop**


[ef91be131ea20c8](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef91be131ea20c8) bsorrentino *2019-10-30 21:40:32*

**update javadoc version of locale**


[4b50d93b4eb6d75](https://github.com/bsorrentino/maven-confluence-plugin/commit/4b50d93b4eb6d75) bsorrentino *2019-10-30 21:36:44*

**Merge branch 'feature/GH-200_add-locale-configuration' of https://github.com/LZaruba/maven-confluence-plugin into LZaruba-feature/GH-200_add-locale-configuration**


[5b27949d3f307de](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b27949d3f307de) bsorrentino *2019-10-30 17:53:28*

**Merge branch 'release/6.5' into develop**


[1297731f5a9d195](https://github.com/bsorrentino/maven-confluence-plugin/commit/1297731f5a9d195) bsorrentino *2019-10-29 10:39:40*

**GH-206 Add possibility to disable url properties resolving**

 * Added new property to override default behavior, improved docs

[c6a71ff8556f0ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6a71ff8556f0ec) Lukas Zaruba *2019-10-20 17:50:46*

**GH-204 Replace invalid characters in labels**

 * Added replacing of invalid characters in labels and tests

[1a0702083713f55](https://github.com/bsorrentino/maven-confluence-plugin/commit/1a0702083713f55) Lukas Zaruba *2019-10-20 16:57:54*

**GH-200 Added locale configuration option**


[6e38ba972878b1a](https://github.com/bsorrentino/maven-confluence-plugin/commit/6e38ba972878b1a) Lukas Zaruba *2019-10-20 11:12:36*


## v6.5
### Generic changes

**Merge branch 'release/6.5'**


[aeaaed650ca5964](https://github.com/bsorrentino/maven-confluence-plugin/commit/aeaaed650ca5964) bsorrentino *2019-10-29 10:39:00*

**update changelog**


[114251b5a53db36](https://github.com/bsorrentino/maven-confluence-plugin/commit/114251b5a53db36) bsorrentino *2019-10-29 10:34:10*

**set release version**


[edff1527dd990ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/edff1527dd990ab) bsorrentino *2019-10-29 10:33:51*

**update changelog**


[df6e4cb163cbb5e](https://github.com/bsorrentino/maven-confluence-plugin/commit/df6e4cb163cbb5e) bsorrentino *2019-10-29 10:32:51*

**update readme**


[5f1703755f208ee](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f1703755f208ee) bsorrentino *2019-10-29 10:32:24*

**Merge branch 'feature/issue#208' into develop**


[16c343452615d07](https://github.com/bsorrentino/maven-confluence-plugin/commit/16c343452615d07) bsorrentino *2019-10-28 10:18:49*

**issue #208**

 * add current Site.Page in processing markdown to understand if
 * ignore-variables has been set

[5c79c9326b16192](https://github.com/bsorrentino/maven-confluence-plugin/commit/5c79c9326b16192) bsorrentino *2019-10-26 09:17:01*

**issue #208**

 * update Site unit test

[eccb84c01cafc1f](https://github.com/bsorrentino/maven-confluence-plugin/commit/eccb84c01cafc1f) bsorrentino *2019-10-26 09:14:54*

**issue #208**

 * add user driven test to verify correct behaviour

[e2bdb10bd15cf93](https://github.com/bsorrentino/maven-confluence-plugin/commit/e2bdb10bd15cf93) bsorrentino *2019-10-26 09:13:01*

**clean code**


[f80541bfb99bcd8](https://github.com/bsorrentino/maven-confluence-plugin/commit/f80541bfb99bcd8) bsorrentino *2019-10-26 09:11:48*

**fix message on page deleting**


[6b46c965cd2cb83](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b46c965cd2cb83) bsorrentino *2019-10-25 22:19:18*

**update doc**


[c3207b6a395b164](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3207b6a395b164) bsorrentino *2019-10-25 22:18:50*

**update user driven test**


[e946dd96a32aae0](https://github.com/bsorrentino/maven-confluence-plugin/commit/e946dd96a32aae0) bsorrentino *2019-10-25 22:18:38*

**move to next developer version**


[b7aa74ca2bf02b7](https://github.com/bsorrentino/maven-confluence-plugin/commit/b7aa74ca2bf02b7) bsorrentino *2019-10-25 22:17:43*

**Merge branch 'release/6.5-beta2' into develop**


[f0e2ad35ac3d1ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0e2ad35ac3d1ed) bsorrentino *2019-10-22 08:28:10*


## v6.5-beta2
### Generic changes

**Merge branch 'release/6.5-beta2'**


[5dcd2c045e21f2a](https://github.com/bsorrentino/maven-confluence-plugin/commit/5dcd2c045e21f2a) bsorrentino *2019-10-22 08:27:51*

**update changelog**


[6a2c2741a2db4a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/6a2c2741a2db4a5) bsorrentino *2019-10-22 08:23:28*

**update readme**


[fc6508120f5c09b](https://github.com/bsorrentino/maven-confluence-plugin/commit/fc6508120f5c09b) bsorrentino *2019-10-22 08:22:22*

**use reporting standard configuration**


[2e1f450db6d7548](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e1f450db6d7548) bsorrentino *2019-10-21 10:34:26*

**move to next release**


[4df8a59b7c490fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/4df8a59b7c490fb) bsorrentino *2019-10-21 08:02:53*

**issue #199**


[de75cbf14b13cf7](https://github.com/bsorrentino/maven-confluence-plugin/commit/de75cbf14b13cf7) bsorrentino *2019-10-17 18:39:00*

**update changelog**


[5487b3c02bc653b](https://github.com/bsorrentino/maven-confluence-plugin/commit/5487b3c02bc653b) bsorrentino *2019-10-16 20:34:29*

**add possibility to deploy plugin documentation to different  confluence server**


[9cdbc301969a1f6](https://github.com/bsorrentino/maven-confluence-plugin/commit/9cdbc301969a1f6) bsorrentino *2019-10-16 10:46:49*

**Merge branch 'hotfix/doc' into develop**


[06de7c25fa499c8](https://github.com/bsorrentino/maven-confluence-plugin/commit/06de7c25fa499c8) bsorrentino *2019-10-14 20:47:24*

**Merge branch 'hotfix/doc'**


[85fb5ddc4123c28](https://github.com/bsorrentino/maven-confluence-plugin/commit/85fb5ddc4123c28) bsorrentino *2019-10-14 20:47:22*

**update documentation**


[e5aaab79556dabc](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5aaab79556dabc) bsorrentino *2019-10-14 20:47:16*

**move to next develop version**


[ab853448b55e005](https://github.com/bsorrentino/maven-confluence-plugin/commit/ab853448b55e005) bsorrentino *2019-10-14 20:15:15*

**Merge branch 'release/6.5-beta1' into develop**


[8a0ba5a44e7fc5f](https://github.com/bsorrentino/maven-confluence-plugin/commit/8a0ba5a44e7fc5f) bsorrentino *2019-10-14 20:14:06*

**Merge branch 'feature/issue#194' into develop**


[bc80ff8f3c36dd3](https://github.com/bsorrentino/maven-confluence-plugin/commit/bc80ff8f3c36dd3) bsorrentino *2019-10-11 16:05:44*

**issue #194 add fallback strategy on page creation**


[0c0b42abb968dd6](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c0b42abb968dd6) bsorrentino *2019-10-11 16:05:01*

**issue #194 integration test refinements**


[7802c6e9e513f0a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7802c6e9e513f0a) bsorrentino *2019-10-11 16:02:30*

**issue #194 derive scrollversions url from confluence url**


[cfb795bc7563570](https://github.com/bsorrentino/maven-confluence-plugin/commit/cfb795bc7563570) bsorrentino *2019-10-09 21:17:40*

**issue #194 update integration test**


[d5492df5952c22e](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5492df5952c22e) bsorrentino *2019-10-09 21:16:20*

**issue #194 add unit test for parse url**


[63f59f68a34b6f0](https://github.com/bsorrentino/maven-confluence-plugin/commit/63f59f68a34b6f0) bsorrentino *2019-10-09 21:15:42*

**issue #194 clean code**


[0232343dece9f36](https://github.com/bsorrentino/maven-confluence-plugin/commit/0232343dece9f36) bsorrentino *2019-10-09 10:22:45*

**issue #194 update package layout for test**


[1bbf5518e002bab](https://github.com/bsorrentino/maven-confluence-plugin/commit/1bbf5518e002bab) bsorrentino *2019-10-09 10:00:09*

**issue #194 update package layout for test**


[7ce1309009fcbe8](https://github.com/bsorrentino/maven-confluence-plugin/commit/7ce1309009fcbe8) bsorrentino *2019-10-09 09:59:58*

**move to next developer version**


[8edd1dc6b9cf5fd](https://github.com/bsorrentino/maven-confluence-plugin/commit/8edd1dc6b9cf5fd) bsorrentino *2019-10-09 00:29:41*

**issue #194 clean code**


[677a831dfe6f37b](https://github.com/bsorrentino/maven-confluence-plugin/commit/677a831dfe6f37b) bsorrentino *2019-10-09 00:29:22*

**issue #194 merge and review PR #197**


[050fe86a2bc2fa7](https://github.com/bsorrentino/maven-confluence-plugin/commit/050fe86a2bc2fa7) bsorrentino *2019-10-09 00:19:49*

**merge pull request #197**

 * refactor package layout

[84a2d1d2a8d016c](https://github.com/bsorrentino/maven-confluence-plugin/commit/84a2d1d2a8d016c) bsorrentino *2019-10-08 10:19:19*

**merge pull request #197**


[043cabc70aa34b3](https://github.com/bsorrentino/maven-confluence-plugin/commit/043cabc70aa34b3) bsorrentino *2019-10-08 10:18:26*

**Merge branch 'feature/scroll-versions-integration' into feature/issue#194**


[349971be46b340d](https://github.com/bsorrentino/maven-confluence-plugin/commit/349971be46b340d) bsorrentino *2019-10-08 10:10:26*

**add retry**


[aebd1a76016e8a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/aebd1a76016e8a7) bsorrentino *2019-10-08 10:08:01*

**add fromRequestAsync**


[7b588cc2f242efa](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b588cc2f242efa) bsorrentino *2019-10-08 10:07:47*

**clean code**


[180a4505e83d2a2](https://github.com/bsorrentino/maven-confluence-plugin/commit/180a4505e83d2a2) bsorrentino *2019-10-08 10:07:04*

**add docker compose file**


[c2fc9e797a7652d](https://github.com/bsorrentino/maven-confluence-plugin/commit/c2fc9e797a7652d) bsorrentino *2019-10-07 16:26:10*

**Merge branch 'feature/scroll-versions-integration' of https://github.com/neko-gg/maven-confluence-plugin into neko-gg-feature/scroll-versions-integration**


[c88bca376eee6bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/c88bca376eee6bd) bsorrentino *2019-10-06 14:04:21*

**Merge branch 'hotfix/changelog' into develop**


[3661d9880bef42f](https://github.com/bsorrentino/maven-confluence-plugin/commit/3661d9880bef42f) bsorrentino *2019-10-01 09:43:32*

**Merge branch 'release/6.4.1' into develop**


[ac2b7b14e627097](https://github.com/bsorrentino/maven-confluence-plugin/commit/ac2b7b14e627097) bsorrentino *2019-10-01 09:35:36*

**scroll version distribution**


[2e8af193d4734de](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e8af193d4734de) bartolomeo sorrentino *2019-10-01 09:01:23*

**add scroll versions confluence service via rest api**


[d74aded3a980190](https://github.com/bsorrentino/maven-confluence-plugin/commit/d74aded3a980190) Nicola Lagnena *2019-09-29 19:55:40*


## v6.5-beta1
### Generic changes

**Merge branch 'release/6.5-beta1'**


[084f028ad1f7e6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/084f028ad1f7e6b) bsorrentino *2019-10-14 20:13:37*

**update changelog**


[0b1113f3541ba24](https://github.com/bsorrentino/maven-confluence-plugin/commit/0b1113f3541ba24) bsorrentino *2019-10-14 20:12:18*

**update doc**


[97b3a5e54e0980c](https://github.com/bsorrentino/maven-confluence-plugin/commit/97b3a5e54e0980c) bsorrentino *2019-10-14 20:11:46*

**update changelog**


[9b200600f3081b1](https://github.com/bsorrentino/maven-confluence-plugin/commit/9b200600f3081b1) bsorrentino *2019-10-11 18:56:07*

**issue #194 update javadoc**


[069605b406b3f76](https://github.com/bsorrentino/maven-confluence-plugin/commit/069605b406b3f76) bsorrentino *2019-10-11 18:51:43*

**issue #194 add integration test**


[34f3ed56d285aa9](https://github.com/bsorrentino/maven-confluence-plugin/commit/34f3ed56d285aa9) bsorrentino *2019-10-11 18:36:55*

**issue #194 integrate ScrollVersions addon configuration in Mojo**


[1dccae737f09ffd](https://github.com/bsorrentino/maven-confluence-plugin/commit/1dccae737f09ffd) bsorrentino *2019-10-11 18:35:59*

**remove deprecated**


[2543b9c78722fe4](https://github.com/bsorrentino/maven-confluence-plugin/commit/2543b9c78722fe4) bsorrentino *2019-10-11 18:34:48*

**remove deprecated**


[acc5495484b4860](https://github.com/bsorrentino/maven-confluence-plugin/commit/acc5495484b4860) bsorrentino *2019-10-11 18:34:39*

**move to next release**


[dd751d1c16f9266](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd751d1c16f9266) bsorrentino *2019-10-11 17:03:58*

**Merge branch 'hotfix/changelog'**


[79285dd8030766c](https://github.com/bsorrentino/maven-confluence-plugin/commit/79285dd8030766c) bsorrentino *2019-10-01 09:43:29*

**update documentation**


[2f8cbdd7ab78965](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f8cbdd7ab78965) bsorrentino *2019-10-01 09:43:24*


## v6.4.1
### Generic changes

**Merge branch 'release/6.4.1'**


[6e51be8e905b8fd](https://github.com/bsorrentino/maven-confluence-plugin/commit/6e51be8e905b8fd) bsorrentino *2019-10-01 09:35:09*

**update changelog**


[4e7515357072bc2](https://github.com/bsorrentino/maven-confluence-plugin/commit/4e7515357072bc2) bsorrentino *2019-10-01 09:34:50*

**update Changelog settings**


[e78fa2503173a5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/e78fa2503173a5b) bsorrentino *2019-10-01 09:34:12*

**move to next release**


[422ed53eb625696](https://github.com/bsorrentino/maven-confluence-plugin/commit/422ed53eb625696) bsorrentino *2019-10-01 09:22:53*

**fix security alert #com.fasterxml.jackson.core:jackson-databind**


[ce3a1541836eb50](https://github.com/bsorrentino/maven-confluence-plugin/commit/ce3a1541836eb50) bsorrentino *2019-10-01 08:56:04*


## v6.4
### Generic changes

**Merge branch 'release/6.4'**


[c15e149b82e253c](https://github.com/bsorrentino/maven-confluence-plugin/commit/c15e149b82e253c) bsorrentino *2019-08-02 16:15:24*

**Merge branch 'hotfix/docs'**


[68fe35851a8947e](https://github.com/bsorrentino/maven-confluence-plugin/commit/68fe35851a8947e) bsorrentino *2019-07-16 14:15:53*

**update readme**


[34e063a08bcc3ae](https://github.com/bsorrentino/maven-confluence-plugin/commit/34e063a08bcc3ae) bsorrentino *2019-07-16 14:15:49*

**Merge branch 'develop'**


[7e02408af647c35](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e02408af647c35) bsorrentino *2019-07-15 19:57:38*

**update changelog**


[cb2a9bb83169ec6](https://github.com/bsorrentino/maven-confluence-plugin/commit/cb2a9bb83169ec6) bsorrentino *2019-07-15 19:57:06*

**Merge branch 'release/6.3.2' into develop**


[e63d9352613dce8](https://github.com/bsorrentino/maven-confluence-plugin/commit/e63d9352613dce8) bsorrentino *2019-07-15 19:54:05*


## v6.3.2
### Generic changes

**Merge branch 'release/6.3.2'**


[eeb9182fba410ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/eeb9182fba410ff) bsorrentino *2019-07-15 19:53:53*

**move to next prod version**


[18f87e9bd08f12a](https://github.com/bsorrentino/maven-confluence-plugin/commit/18f87e9bd08f12a) bsorrentino *2019-07-15 19:38:40*

**Merge branch 'feature/issue#191' into develop**


[bea49be8d57cb49](https://github.com/bsorrentino/maven-confluence-plugin/commit/bea49be8d57cb49) bsorrentino *2019-07-15 07:47:50*

**fix issue #191**


[f3f2397c47dc2c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/f3f2397c47dc2c5) bsorrentino *2019-07-12 15:26:43*

**move to next develop version**


[ced975538a918d0](https://github.com/bsorrentino/maven-confluence-plugin/commit/ced975538a918d0) bsorrentino *2019-07-12 15:26:33*

**clean code**


[a6de091b6cba4d2](https://github.com/bsorrentino/maven-confluence-plugin/commit/a6de091b6cba4d2) bsorrentino *2019-07-12 13:11:17*

**update test to verify #191**


[8582b97232df30c](https://github.com/bsorrentino/maven-confluence-plugin/commit/8582b97232df30c) bsorrentino *2019-07-10 16:02:56*

**update doc**


[cc3d763965c975a](https://github.com/bsorrentino/maven-confluence-plugin/commit/cc3d763965c975a) bsorrentino *2019-06-12 12:48:19*

**update doc**


[a497a5076be540c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a497a5076be540c) bsorrentino *2019-06-12 12:47:11*

**update doc**


[b440eb9c59b4ab6](https://github.com/bsorrentino/maven-confluence-plugin/commit/b440eb9c59b4ab6) bsorrentino *2019-06-12 12:44:34*

**update doc**


[2655c57238317b1](https://github.com/bsorrentino/maven-confluence-plugin/commit/2655c57238317b1) bsorrentino *2019-06-12 12:42:42*

**update documentation**


[26dee555c4c43ad](https://github.com/bsorrentino/maven-confluence-plugin/commit/26dee555c4c43ad) bsorrentino *2019-06-12 12:37:44*

**Merge branch 'hotfix/docs' into develop**


[0e9a5a6917b960e](https://github.com/bsorrentino/maven-confluence-plugin/commit/0e9a5a6917b960e) bsorrentino *2019-06-12 10:34:42*

**Merge branch 'hotfix/docs'**


[f92eb7b426e6937](https://github.com/bsorrentino/maven-confluence-plugin/commit/f92eb7b426e6937) bsorrentino *2019-06-12 10:34:40*

**update readme**


[40a6fe7d1c7b787](https://github.com/bsorrentino/maven-confluence-plugin/commit/40a6fe7d1c7b787) bsorrentino *2019-06-12 10:33:59*

**Merge branch 'hotfix/changelog' into develop**


[a2b274d9a783106](https://github.com/bsorrentino/maven-confluence-plugin/commit/a2b274d9a783106) bsorrentino *2019-06-12 08:56:50*

**Merge branch 'hotfix/changelog'**


[4e379a63fbd3ccc](https://github.com/bsorrentino/maven-confluence-plugin/commit/4e379a63fbd3ccc) bsorrentino *2019-06-12 08:56:48*

**update changelog**


[c496bea1bd57fa9](https://github.com/bsorrentino/maven-confluence-plugin/commit/c496bea1bd57fa9) bsorrentino *2019-06-12 08:56:32*

**Merge branch 'release/6.3.1' into develop**


[0c99c795b6e106f](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c99c795b6e106f) bsorrentino *2019-06-12 08:54:22*


## v6.3.1
### Generic changes

**Merge branch 'release/6.3.1'**


[5b1bdf4a8fc2fa9](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b1bdf4a8fc2fa9) bsorrentino *2019-06-12 08:54:08*

**update CHANGELOG**


[1ac14d9ff50b9ad](https://github.com/bsorrentino/maven-confluence-plugin/commit/1ac14d9ff50b9ad) bsorrentino *2019-06-12 08:45:37*

**update submodule**


[d78af19f40612c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/d78af19f40612c2) bsorrentino *2019-06-12 08:41:43*

**fix security alert - com.fasterxml.jackson.core:jackson-databind**


[fc9dbdca2ded886](https://github.com/bsorrentino/maven-confluence-plugin/commit/fc9dbdca2ded886) bsorrentino *2019-06-12 08:35:25*

**Merge branch 'hotfix/readme' into develop**


[fb1ba4e4e591b5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/fb1ba4e4e591b5b) bsorrentino *2019-03-28 16:22:13*

**Merge branch 'hotfix/readme'**


[3cfead61cfd2cfc](https://github.com/bsorrentino/maven-confluence-plugin/commit/3cfead61cfd2cfc) bsorrentino *2019-03-28 16:22:10*

**update readme**


[f94d8dfac9cadb7](https://github.com/bsorrentino/maven-confluence-plugin/commit/f94d8dfac9cadb7) bsorrentino *2019-03-28 16:22:05*

**Merge branch 'hotfix/changelog' into develop**


[3cff4ecdc072ff7](https://github.com/bsorrentino/maven-confluence-plugin/commit/3cff4ecdc072ff7) bsorrentino *2019-03-28 15:47:18*

**Merge branch 'hotfix/changelog'**


[0a70657875cf5a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/0a70657875cf5a5) bsorrentino *2019-03-28 15:47:16*

**update changelog**


[2fe8de8bec7b582](https://github.com/bsorrentino/maven-confluence-plugin/commit/2fe8de8bec7b582) bsorrentino *2019-03-28 15:47:06*

**Merge branch 'release/6.3' into develop**


[654457a8110114b](https://github.com/bsorrentino/maven-confluence-plugin/commit/654457a8110114b) bsorrentino *2019-03-28 15:44:38*


## v6.3
### Generic changes

**Merge branch 'release/6.3'**


[54039a294bce7c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/54039a294bce7c3) bsorrentino *2019-03-28 15:44:27*

**move to next version**


[a4cebcecfd57c7f](https://github.com/bsorrentino/maven-confluence-plugin/commit/a4cebcecfd57c7f) bsorrentino *2019-03-28 15:41:30*

**clean code**


[fcdb726e4cbe50b](https://github.com/bsorrentino/maven-confluence-plugin/commit/fcdb726e4cbe50b) bsorrentino *2019-03-26 19:57:02*

**Merge branch 'feature/issue#189' into develop**


[a35a485dc5d6c21](https://github.com/bsorrentino/maven-confluence-plugin/commit/a35a485dc5d6c21) bsorrentino *2019-03-26 19:51:50*

**issue #189**


[fe0642f3ba1548c](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe0642f3ba1548c) bsorrentino *2019-03-26 19:46:19*

**issue #189**


[29e6f9ce768c457](https://github.com/bsorrentino/maven-confluence-plugin/commit/29e6f9ce768c457) bsorrentino *2019-03-26 19:46:05*

**Merge branch 'hotfix/fix_doc_issue#187' into develop**


[cd413d357ab9a3f](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd413d357ab9a3f) bsorrentino *2019-03-22 09:05:00*

**Merge branch 'hotfix/fix_doc_issue#187'**


[6b8f9ad112cbbf4](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b8f9ad112cbbf4) bsorrentino *2019-03-22 09:04:57*

**update documentation - fix #187**


[21f90a0ce7ec9c9](https://github.com/bsorrentino/maven-confluence-plugin/commit/21f90a0ce7ec9c9) bsorrentino *2019-03-22 09:01:07*

**#187 add test to verify issue**


[1ce5e41f65d6871](https://github.com/bsorrentino/maven-confluence-plugin/commit/1ce5e41f65d6871) bsorrentino *2019-03-21 16:17:16*

**update doc**


[6b3a646d8d61f4e](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b3a646d8d61f4e) bsorrentino *2019-02-12 11:12:02*

**move to nex dev version**


[23763afc7f2b202](https://github.com/bsorrentino/maven-confluence-plugin/commit/23763afc7f2b202) bsorrentino *2019-02-07 17:16:36*

**fix problem on xmlrpc - getInt recursive call**


[cb5b800661b0473](https://github.com/bsorrentino/maven-confluence-plugin/commit/cb5b800661b0473) bsorrentino *2019-02-07 17:14:44*

**update changelog**


[715064c9a913869](https://github.com/bsorrentino/maven-confluence-plugin/commit/715064c9a913869) bsorrentino *2019-02-07 17:03:43*

**Merge branch 'release/6.2' into develop**


[dbc9419599445fa](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbc9419599445fa) bsorrentino *2019-02-07 17:01:23*


## v6.2
### Generic changes

**Merge branch 'release/6.2'**


[91648e499b5fb5d](https://github.com/bsorrentino/maven-confluence-plugin/commit/91648e499b5fb5d) bsorrentino *2019-02-07 17:01:12*

**update ignore**


[e239904cb787ec0](https://github.com/bsorrentino/maven-confluence-plugin/commit/e239904cb787ec0) bsorrentino *2019-02-07 17:00:40*

**update ignore**


[84aa61b73473210](https://github.com/bsorrentino/maven-confluence-plugin/commit/84aa61b73473210) bsorrentino *2019-02-07 17:00:15*

**update readme**


[bec2a160f918e4c](https://github.com/bsorrentino/maven-confluence-plugin/commit/bec2a160f918e4c) bsorrentino *2019-02-07 15:18:45*

**promote version**


[1eed2fd134a4d87](https://github.com/bsorrentino/maven-confluence-plugin/commit/1eed2fd134a4d87) bsorrentino *2019-02-07 15:17:45*

**clean code**


[53d7a051dbdcef4](https://github.com/bsorrentino/maven-confluence-plugin/commit/53d7a051dbdcef4) bsorrentino *2019-02-03 12:18:27*

**clean code**


[e34707a9410123a](https://github.com/bsorrentino/maven-confluence-plugin/commit/e34707a9410123a) bsorrentino *2019-02-03 12:14:46*

**clean code**


[c417e1009c81312](https://github.com/bsorrentino/maven-confluence-plugin/commit/c417e1009c81312) bsorrentino *2019-02-03 11:59:52*

**clean code**


[7b1b37605eae9c6](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b1b37605eae9c6) bsorrentino *2019-02-03 11:56:20*

**clean code**


[79d177087e07f42](https://github.com/bsorrentino/maven-confluence-plugin/commit/79d177087e07f42) bsorrentino *2019-02-03 11:55:22*

**clean code**


[911971d06d774a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/911971d06d774a8) bsorrentino *2019-02-03 11:52:48*

**clean code**


[7b13808569014ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b13808569014ff) bsorrentino *2019-02-03 11:52:29*

**clean code**


[aaa8af98e3f14f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/aaa8af98e3f14f5) bsorrentino *2019-02-03 11:51:53*

**clean code**


[257dfc0f8aae07f](https://github.com/bsorrentino/maven-confluence-plugin/commit/257dfc0f8aae07f) bsorrentino *2019-02-03 11:51:08*

**clean code**


[9230fe6d4c5e204](https://github.com/bsorrentino/maven-confluence-plugin/commit/9230fe6d4c5e204) bsorrentino *2019-02-03 11:50:42*

**remove deprecation**


[651f33abae20f0a](https://github.com/bsorrentino/maven-confluence-plugin/commit/651f33abae20f0a) bsorrentino *2019-02-03 11:49:31*

**clean code**


[7a769860d10e656](https://github.com/bsorrentino/maven-confluence-plugin/commit/7a769860d10e656) bsorrentino *2019-02-03 11:46:42*

**unit test from version 4.8.3 => 4.12**


[77143f5bc290a18](https://github.com/bsorrentino/maven-confluence-plugin/commit/77143f5bc290a18) bsorrentino *2019-02-03 11:46:29*

**generate confluence doc on install phase**


[7de98f93f1ff2d9](https://github.com/bsorrentino/maven-confluence-plugin/commit/7de98f93f1ff2d9) bsorrentino *2019-02-01 18:11:48*

**move to next dev version**


[9ba5d62710304d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ba5d62710304d7) bsorrentino *2019-02-01 13:07:02*

**move to parboiled-java version 1.1.7 to fix Pegdown Custom ParserPlugin**

 * binding failed
 * java.lang.RuntimeException: Error creating extended parser class: null

[fab238c846ec55c](https://github.com/bsorrentino/maven-confluence-plugin/commit/fab238c846ec55c) bsorrentino *2019-02-01 13:06:46*

**fix plugin doc**


[aab3ad1fae2a3f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/aab3ad1fae2a3f3) bsorrentino *2019-02-01 13:04:39*

**refine confluence plugin doc**


[f507c6bfe9fe8f6](https://github.com/bsorrentino/maven-confluence-plugin/commit/f507c6bfe9fe8f6) bsorrentino *2019-02-01 13:03:48*

**clean code**


[282b8c8d4585e53](https://github.com/bsorrentino/maven-confluence-plugin/commit/282b8c8d4585e53) bsorrentino *2019-02-01 13:03:19*

**clean code**


[2dc0c9fb7cb7256](https://github.com/bsorrentino/maven-confluence-plugin/commit/2dc0c9fb7cb7256) bsorrentino *2019-02-01 10:15:01*

**Merge branch 'hotfix/changelog' into develop**


[dcb57ae245406ce](https://github.com/bsorrentino/maven-confluence-plugin/commit/dcb57ae245406ce) bsorrentino *2019-01-16 11:43:17*

**Merge branch 'hotfix/changelog'**


[46b393f6d9e103f](https://github.com/bsorrentino/maven-confluence-plugin/commit/46b393f6d9e103f) bsorrentino *2019-01-16 11:43:14*

**update changelog**


[49287762f3703b0](https://github.com/bsorrentino/maven-confluence-plugin/commit/49287762f3703b0) bsorrentino *2019-01-16 11:43:04*

**Merge branch 'release/6.1' into develop**


[07fda207b883cf5](https://github.com/bsorrentino/maven-confluence-plugin/commit/07fda207b883cf5) bsorrentino *2019-01-16 11:39:05*


## v6.1
### Generic changes

**Merge branch 'release/6.1'**


[ab294f345cd9e47](https://github.com/bsorrentino/maven-confluence-plugin/commit/ab294f345cd9e47) bsorrentino *2019-01-16 11:38:55*

**update readme**


[7904816919c500e](https://github.com/bsorrentino/maven-confluence-plugin/commit/7904816919c500e) bsorrentino *2019-01-16 11:38:37*

**move to next release**


[5e730dbfd5978a9](https://github.com/bsorrentino/maven-confluence-plugin/commit/5e730dbfd5978a9) bsorrentino *2019-01-16 11:30:48*

**fix issue #186 move to next developer version**


[d8a9853f38dadae](https://github.com/bsorrentino/maven-confluence-plugin/commit/d8a9853f38dadae) bsorrentino *2019-01-16 11:22:27*

**Merge branch 'hotfix/new' into develop**


[0c01dfef83f6fc0](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c01dfef83f6fc0) bsorrentino *2019-01-14 18:30:57*

**Merge branch 'hotfix/new'**


[c5fd3998130e097](https://github.com/bsorrentino/maven-confluence-plugin/commit/c5fd3998130e097) bsorrentino *2019-01-14 18:30:54*

**update readme**


[906ad52e9b9934c](https://github.com/bsorrentino/maven-confluence-plugin/commit/906ad52e9b9934c) bsorrentino *2019-01-14 18:29:46*

**Merge branch 'hotfix/changelog' into develop**


[8b16744a9c7a28d](https://github.com/bsorrentino/maven-confluence-plugin/commit/8b16744a9c7a28d) bsorrentino *2019-01-09 17:19:13*

**Merge branch 'release/6.0' into develop**


[34c30e69c00bb9c](https://github.com/bsorrentino/maven-confluence-plugin/commit/34c30e69c00bb9c) bsorrentino *2019-01-09 17:10:15*


## v6.0
### Generic changes

**Merge branch 'hotfix/changelog'**


[434aaf861e1c650](https://github.com/bsorrentino/maven-confluence-plugin/commit/434aaf861e1c650) bsorrentino *2019-01-09 17:19:11*

**update changelog config**


[9155dbb9eaa1050](https://github.com/bsorrentino/maven-confluence-plugin/commit/9155dbb9eaa1050) bsorrentino *2019-01-09 17:19:02*

**Merge remote-tracking branch 'origin/master'**


[ce7555feb897bf6](https://github.com/bsorrentino/maven-confluence-plugin/commit/ce7555feb897bf6) bsorrentino *2019-01-09 17:12:38*

**Merge branch 'release/6.0'**


[9525f580892d3e7](https://github.com/bsorrentino/maven-confluence-plugin/commit/9525f580892d3e7) bsorrentino *2019-01-09 17:10:02*

**prepare release 6.0**


[41ba8b0d672dd4d](https://github.com/bsorrentino/maven-confluence-plugin/commit/41ba8b0d672dd4d) bsorrentino *2019-01-09 17:04:24*

**move to next release**


[b277612cbf79f13](https://github.com/bsorrentino/maven-confluence-plugin/commit/b277612cbf79f13) bsorrentino *2019-01-08 21:51:25*

**update changelog**


[ca520fecf2df5bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/ca520fecf2df5bb) bsorrentino *2019-01-08 21:43:58*

**Merge branch feature/issue#183 into develop**


[9dfc2805c784d11](https://github.com/bsorrentino/maven-confluence-plugin/commit/9dfc2805c784d11) Bartolomeo Sorrentino *2019-01-05 20:27:03*

**Merge branch 'hotfix/security_alert'**


[dd14a8aa1c2095c](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd14a8aa1c2095c) Bartolomeo Sorrentino *2019-01-05 20:24:17*

**fix security alert**


[11e6f4e94d434d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/11e6f4e94d434d8) Bartolomeo Sorrentino *2019-01-05 20:23:00*

**Merge branch 'feature/issue#183' into develop**


[a35fe349c9ace20](https://github.com/bsorrentino/maven-confluence-plugin/commit/a35fe349c9ace20) Bartolomeo Sorrentino *2019-01-05 20:10:33*

**add maven prerequisites**


[2ddbcdd4d3fa069](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ddbcdd4d3fa069) Bartolomeo Sorrentino *2019-01-05 20:10:11*

**update to be compatible with java 11**


[d92a4ea2b1d518a](https://github.com/bsorrentino/maven-confluence-plugin/commit/d92a4ea2b1d518a) Bartolomeo Sorrentino *2019-01-05 18:44:57*

**Merge branch 'hotfix/readme' into develop**


[a2edc284f88812c](https://github.com/bsorrentino/maven-confluence-plugin/commit/a2edc284f88812c) bsorrentino *2018-11-26 23:14:14*

**Merge branch 'hotfix/readme'**


[f8c11b1320399a4](https://github.com/bsorrentino/maven-confluence-plugin/commit/f8c11b1320399a4) bsorrentino *2018-11-26 23:14:12*

**update readme**


[1ba1776852f8407](https://github.com/bsorrentino/maven-confluence-plugin/commit/1ba1776852f8407) bsorrentino *2018-11-26 23:13:37*

**new release 6.0-rc4**


[9281e4a7bb4bf81](https://github.com/bsorrentino/maven-confluence-plugin/commit/9281e4a7bb4bf81) bsorrentino *2018-11-26 22:58:41*

**Merge branch 'feature/issue#182' into develop**


[9a7d656c03f4fdc](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a7d656c03f4fdc) bsorrentino *2018-11-25 20:02:14*

**update changelog**


[c1efd3ba10366da](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1efd3ba10366da) bsorrentino *2018-11-25 20:01:55*

**#182 - add check on name**


[d57d2480497936c](https://github.com/bsorrentino/maven-confluence-plugin/commit/d57d2480497936c) bsorrentino *2018-11-25 20:01:06*

**#182 - add unit tests**


[9e178bfe1d43171](https://github.com/bsorrentino/maven-confluence-plugin/commit/9e178bfe1d43171) bsorrentino *2018-11-25 20:00:46*

**clean code**


[d1031aaafe8ce04](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1031aaafe8ce04) bsorrentino *2018-11-23 13:08:56*

**Merge pull request #181 from qwazer/maven-confluence-plugin-180**

 * Encoding of special chars in confluence page title

[848e2b3d6b6b47f](https://github.com/bsorrentino/maven-confluence-plugin/commit/848e2b3d6b6b47f) bsorrentino *2018-10-30 21:35:29*

**clean code**


[37abf19602cd962](https://github.com/bsorrentino/maven-confluence-plugin/commit/37abf19602cd962) bsorrentino *2018-10-29 16:04:18*

**Merge branch 'feature/issue#179' into develop**


[cd20ada9cfcb6ed](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd20ada9cfcb6ed) bsorrentino *2018-10-29 15:28:45*

**fix issue #179**


[d656f720d181af4](https://github.com/bsorrentino/maven-confluence-plugin/commit/d656f720d181af4) bsorrentino *2018-10-29 15:28:21*

**clean code**


[4eb40ca92ff42d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/4eb40ca92ff42d5) bsorrentino *2018-10-29 15:04:17*

**issue #178 - reuse token in XMLRPC protocol**


[c8ca5c1e5c09b17](https://github.com/bsorrentino/maven-confluence-plugin/commit/c8ca5c1e5c09b17) bsorrentino *2018-10-26 16:13:12*

**update submodule**


[ff2475099b79702](https://github.com/bsorrentino/maven-confluence-plugin/commit/ff2475099b79702) bsorrentino *2018-10-23 10:46:01*

**Merge branch 'hotfix/docs' into develop**


[34d764aaf499d7f](https://github.com/bsorrentino/maven-confluence-plugin/commit/34d764aaf499d7f) bsorrentino *2018-10-12 20:39:48*

**update changelog template**


[0eb13d12db88490](https://github.com/bsorrentino/maven-confluence-plugin/commit/0eb13d12db88490) bsorrentino *2018-09-26 15:26:31*

**move to next developer version**


[4b12184ad37a078](https://github.com/bsorrentino/maven-confluence-plugin/commit/4b12184ad37a078) bsorrentino *2018-09-26 15:20:10*

**Merge branch 'release/6.0-rc3' into develop**


[ec27e3770b29fa7](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec27e3770b29fa7) bsorrentino *2018-09-26 15:17:27*


## v6.0-rc4
### Generic changes

**Merge branch 'release/6.0-rc4'**


[446bc3a0577034d](https://github.com/bsorrentino/maven-confluence-plugin/commit/446bc3a0577034d) bsorrentino *2018-11-26 22:55:39*

**update gcp plugin**


[e115a40691ef998](https://github.com/bsorrentino/maven-confluence-plugin/commit/e115a40691ef998) bsorrentino *2018-11-26 22:55:21*

**prepare for  next version 6.0-rc4**


[62fde8a4275a7f4](https://github.com/bsorrentino/maven-confluence-plugin/commit/62fde8a4275a7f4) bsorrentino *2018-11-25 20:49:32*

**#182 - add unit tests**


[8596b2545495fae](https://github.com/bsorrentino/maven-confluence-plugin/commit/8596b2545495fae) bsorrentino *2018-11-25 20:45:29*

**update changelog**


[78b33e2b5d01606](https://github.com/bsorrentino/maven-confluence-plugin/commit/78b33e2b5d01606) bsorrentino *2018-11-25 20:45:29*

**#182 - add check on name**


[2eed4ba59dfd2a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/2eed4ba59dfd2a7) bsorrentino *2018-11-25 20:45:29*

**clean code**


[240e852de6b4ced](https://github.com/bsorrentino/maven-confluence-plugin/commit/240e852de6b4ced) bsorrentino *2018-11-25 20:45:29*

**Encoding of special chars in confluence page title**

 * update version of squareup okhttp3

[c82f69d678fc80a](https://github.com/bsorrentino/maven-confluence-plugin/commit/c82f69d678fc80a) qwazer *2018-10-30 09:24:01*

**Merge branch 'hotfix/docs'**


[10772913e6560c0](https://github.com/bsorrentino/maven-confluence-plugin/commit/10772913e6560c0) bsorrentino *2018-10-12 20:39:44*

**update documentation**


[74e1eb09223c3ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/74e1eb09223c3ec) bsorrentino *2018-10-12 20:35:59*

**update documentation**


[1b211eccbd3a73a](https://github.com/bsorrentino/maven-confluence-plugin/commit/1b211eccbd3a73a) bsorrentino *2018-10-12 20:35:35*

**update changelog**


[3aeb3f10496afdc](https://github.com/bsorrentino/maven-confluence-plugin/commit/3aeb3f10496afdc) bsorrentino *2018-09-26 15:19:11*


## v6.0-rc3
### Generic changes

**Merge branch 'release/6.0-rc3'**


[509b5b571d39b62](https://github.com/bsorrentino/maven-confluence-plugin/commit/509b5b571d39b62) bsorrentino *2018-09-26 15:17:26*

**move to release 6.0-rc3**


[68e41ca166a945e](https://github.com/bsorrentino/maven-confluence-plugin/commit/68e41ca166a945e) bsorrentino *2018-09-26 15:16:58*

**changelog refinements**


[213058ebca090e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/213058ebca090e2) bsorrentino *2018-09-26 15:08:22*

**changelog plugin refinements**


[911dd1563584ce2](https://github.com/bsorrentino/maven-confluence-plugin/commit/911dd1563584ce2) bsorrentino *2018-09-26 13:44:05*

**Merge branch 'feature/gitchangelog' into develop**


[2e1e8554f80990f](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e1e8554f80990f) bsorrentino *2018-09-26 13:27:34*

**add support for git-changelog-maven-plugin**


[95d240f04734eae](https://github.com/bsorrentino/maven-confluence-plugin/commit/95d240f04734eae) bsorrentino *2018-09-26 13:27:23*

**Merge branch 'develop' into feature/gitchangelog**

 * # Conflicts:
 * #	.gitignore

[b833c6103ec4ab2](https://github.com/bsorrentino/maven-confluence-plugin/commit/b833c6103ec4ab2) bsorrentino *2018-09-26 13:26:36*

**add deploystate for internal deployment**


[e03f887c5e6b343](https://github.com/bsorrentino/maven-confluence-plugin/commit/e03f887c5e6b343) bsorrentino *2018-09-04 16:43:58*

**Merge branch 'develop' into qwazer-maven-confluence-plugin-167**


[8ef6249b3121c54](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ef6249b3121c54) bsorrentino *2018-09-04 16:37:54*

**Merge branch 'feature/issue#170' into develop**


[0f5c724abf82af1](https://github.com/bsorrentino/maven-confluence-plugin/commit/0f5c724abf82af1) bsorrentino *2018-09-04 16:36:42*

**issue #170 -**

 * remove the processor methods from Site to SiteProcessor
 * remove the print methods from Site to SitePrinter
 * refactor usage

[1480fda8bf3add5](https://github.com/bsorrentino/maven-confluence-plugin/commit/1480fda8bf3add5) bsorrentino *2018-09-04 16:28:40*

**remove warning**


[2c379f919d55fcb](https://github.com/bsorrentino/maven-confluence-plugin/commit/2c379f919d55fcb) bsorrentino *2018-09-04 16:25:42*

**Merge branch 'develop' into qwazer-maven-confluence-plugin-167**


[ffe4954a5c2dea4](https://github.com/bsorrentino/maven-confluence-plugin/commit/ffe4954a5c2dea4) bsorrentino *2018-09-02 14:10:58*

**update ignore**


[b2aa2dff9d5e71c](https://github.com/bsorrentino/maven-confluence-plugin/commit/b2aa2dff9d5e71c) bsorrentino *2018-09-02 14:10:02*

**add changelog plugin**


[3e3bc3330d99a61](https://github.com/bsorrentino/maven-confluence-plugin/commit/3e3bc3330d99a61) bsorrentino *2018-09-02 14:08:52*

**update ignore**


[f9d392fcef36dea](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9d392fcef36dea) bsorrentino *2018-09-02 14:08:36*

**Merge branch 'feature/issue#169' into develop**


[716fec5ffc658e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/716fec5ffc658e2) bsorrentino *2018-08-31 17:28:07*

**issue #169 - implementation using jackson**


[2a9dcb51c3be4bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/2a9dcb51c3be4bb) bsorrentino *2018-08-31 17:27:49*

**issue #169 - unit tests**


[5c1e4775bd26cb0](https://github.com/bsorrentino/maven-confluence-plugin/commit/5c1e4775bd26cb0) bsorrentino *2018-08-31 17:27:07*

**setup json pretty printing**


[af0403e6f59978e](https://github.com/bsorrentino/maven-confluence-plugin/commit/af0403e6f59978e) areshetnikov *2018-08-31 12:57:44*

**clean code**


[da3497e33068c3d](https://github.com/bsorrentino/maven-confluence-plugin/commit/da3497e33068c3d) areshetnikov *2018-08-31 08:54:36*

**change STORAGE_NAME from state.json to confluence-reporting-maven-plugin-storage.json**


[84a1ce5f934eedf](https://github.com/bsorrentino/maven-confluence-plugin/commit/84a1ce5f934eedf) areshetnikov *2018-08-31 08:51:52*

**maven-confluence-plugin-167 Comparing by content md5 hash in DeployStateManager.isUpdated**

 * implement, use Use DigestUtils from Apache Commons Codec library to calculate md5

[8270e4861ff2d5e](https://github.com/bsorrentino/maven-confluence-plugin/commit/8270e4861ff2d5e) areshetnikov *2018-08-31 08:18:29*

**maven-confluence-plugin-167 Comparing by content md5 hash in DeployStateManager.isUpdated**

 * add unit tests for existing functionality

[2159d908d0bd082](https://github.com/bsorrentino/maven-confluence-plugin/commit/2159d908d0bd082) areshetnikov *2018-08-31 07:30:37*

**Merge branch 'hotfix/remove_vscode' into develop**


[a5961faff183daa](https://github.com/bsorrentino/maven-confluence-plugin/commit/a5961faff183daa) bsorrentino *2018-08-28 16:32:46*

**Merge branch 'hotfix/remove_vscode'**


[503f3e6720366be](https://github.com/bsorrentino/maven-confluence-plugin/commit/503f3e6720366be) bsorrentino *2018-08-28 16:32:16*

**remove vscode stuff**


[2e50fe58eab6e97](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e50fe58eab6e97) bsorrentino *2018-08-28 16:32:09*

**remove vscode stuff**


[5163d596db9beba](https://github.com/bsorrentino/maven-confluence-plugin/commit/5163d596db9beba) bsorrentino *2018-08-28 16:31:24*

**Merge branch 'feature/#164' into develop**


[e8506c9dd37fbb8](https://github.com/bsorrentino/maven-confluence-plugin/commit/e8506c9dd37fbb8) bsorrentino *2018-08-28 14:41:42*

**issue #164**

 * add support on tag site of otional attribute &#x27;space-key&#x27;
 * add attribute on home of optional attributes &#x27;parent-page&#x27; and
 * &#x27;parent-page-id&#x27;

[93f8dab4fe97062](https://github.com/bsorrentino/maven-confluence-plugin/commit/93f8dab4fe97062) bsorrentino *2018-08-28 14:41:26*

**move to next developer version**


[921379decb8a49e](https://github.com/bsorrentino/maven-confluence-plugin/commit/921379decb8a49e) bsorrentino *2018-08-28 14:38:54*

**Merge branch 'hotfix/update_xsd' into develop**


[944ddd581a65068](https://github.com/bsorrentino/maven-confluence-plugin/commit/944ddd581a65068) bsorrentino *2018-08-25 18:34:00*

**Merge branch 'hotfix/update_xsd'**


[e152b10c4f1332f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e152b10c4f1332f) bsorrentino *2018-08-25 18:33:58*

**issue #162 - update schema to 6.0**


[1a51c934a8b6e32](https://github.com/bsorrentino/maven-confluence-plugin/commit/1a51c934a8b6e32) bsorrentino *2018-08-25 18:33:38*

**Merge branch 'hotfix/xsd' into develop**


[d76fcbeac23722b](https://github.com/bsorrentino/maven-confluence-plugin/commit/d76fcbeac23722b) bsorrentino *2018-08-25 18:12:02*

**Merge branch 'hotfix/xsd'**


[c58e4ec46e0da1e](https://github.com/bsorrentino/maven-confluence-plugin/commit/c58e4ec46e0da1e) bsorrentino *2018-08-25 18:11:55*

**issue #162 - add new site schema 6.0**


[f4ac79f3c57fff9](https://github.com/bsorrentino/maven-confluence-plugin/commit/f4ac79f3c57fff9) bsorrentino *2018-08-25 18:11:42*

**issue #162 - fix 'ignore-variables' schema generation**


[67a031038e44da2](https://github.com/bsorrentino/maven-confluence-plugin/commit/67a031038e44da2) bsorrentino *2018-08-25 18:08:40*

**remove .vscode from distribution**


[cad499deb734b83](https://github.com/bsorrentino/maven-confluence-plugin/commit/cad499deb734b83) bsorrentino *2018-08-23 09:05:57*

**update ignore**


[e07e96b28f2fca3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e07e96b28f2fca3) bsorrentino *2018-08-23 09:05:09*

**Merge branch 'release/6.0-rc2' into develop**


[7bd18c747fed1cc](https://github.com/bsorrentino/maven-confluence-plugin/commit/7bd18c747fed1cc) bsorrentino *2018-08-14 09:52:50*


## v6.0-rc2
### Generic changes

**Merge branch 'release/6.0-rc2'**


[0225e7e06ae2584](https://github.com/bsorrentino/maven-confluence-plugin/commit/0225e7e06ae2584) bsorrentino *2018-08-14 09:52:37*

**release 6.0-rc2**


[98a6270ab29f420](https://github.com/bsorrentino/maven-confluence-plugin/commit/98a6270ab29f420) bsorrentino *2018-08-14 09:52:19*

**Merge branch 'feature/issue#160' into develop**


[db5d216aba7e633](https://github.com/bsorrentino/maven-confluence-plugin/commit/db5d216aba7e633) bsorrentino *2018-08-14 09:43:53*

**remove warning**


[470c5beaab6ac96](https://github.com/bsorrentino/maven-confluence-plugin/commit/470c5beaab6ac96) bsorrentino *2018-08-13 15:12:01*

**issue #160 - mapToArray 2 mapToStream checking if it is an array o single object**


[187e432e6dc7c65](https://github.com/bsorrentino/maven-confluence-plugin/commit/187e432e6dc7c65) bsorrentino *2018-08-13 14:51:34*

**move to next development version**


[9dcb85a58ab4ac5](https://github.com/bsorrentino/maven-confluence-plugin/commit/9dcb85a58ab4ac5) bsorrentino *2018-08-13 14:42:01*

**update test**


[46e4e1c1bfd4eed](https://github.com/bsorrentino/maven-confluence-plugin/commit/46e4e1c1bfd4eed) bsorrentino *2018-08-12 17:29:35*

**Merge branch 'release/6.0-rc1' into develop**


[f33e35e69eb54d0](https://github.com/bsorrentino/maven-confluence-plugin/commit/f33e35e69eb54d0) bsorrentino *2018-07-24 10:28:13*


## v6.0-rc1
### Generic changes

**Merge branch 'release/6.0-rc1'**


[592a3db7586231c](https://github.com/bsorrentino/maven-confluence-plugin/commit/592a3db7586231c) bsorrentino *2018-07-24 10:27:59*

**prepare for release 6.0-rc1**


[d9e491325f59d1d](https://github.com/bsorrentino/maven-confluence-plugin/commit/d9e491325f59d1d) bsorrentino *2018-07-24 10:27:39*

**Merge branch 'feature/issue#158' into develop**


[74d33a5993d36fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/74d33a5993d36fb) bsorrentino *2018-07-24 09:53:55*

**issue #158 - update test**


[40fb661453bacd8](https://github.com/bsorrentino/maven-confluence-plugin/commit/40fb661453bacd8) bsorrentino *2018-07-22 21:08:17*

**issue #158 - xmlrpc: avoid update page without content**


[2567b2d24b6c4e1](https://github.com/bsorrentino/maven-confluence-plugin/commit/2567b2d24b6c4e1) bsorrentino *2018-07-22 21:01:45*

**remove netbeans files**


[9ac419911490aa2](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ac419911490aa2) bsorrentino *2018-07-16 09:13:59*

**test issue158**


[8452558cf229bbe](https://github.com/bsorrentino/maven-confluence-plugin/commit/8452558cf229bbe) bsorrentino *2018-07-16 09:08:32*

**Merge branch 'hotfix/#157' into develop**


[a90d2fe602e4f81](https://github.com/bsorrentino/maven-confluence-plugin/commit/a90d2fe602e4f81) bsorrentino *2018-07-10 23:32:31*

**Merge branch 'hotfix/#157'**


[243b7d9b545e570](https://github.com/bsorrentino/maven-confluence-plugin/commit/243b7d9b545e570) bsorrentino *2018-07-10 23:32:30*

**issue #157 - update doc**


[02e5b695b500dcf](https://github.com/bsorrentino/maven-confluence-plugin/commit/02e5b695b500dcf) bsorrentino *2018-07-10 23:32:20*

**Merge branch 'release/6.0-beta2' into develop**


[ab159cb745407d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/ab159cb745407d4) bsorrentino *2018-07-09 17:06:22*


## v6.0-beta2
### Generic changes

**Merge branch 'release/6.0-beta2'**


[2e2e38e05c644a2](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e2e38e05c644a2) bsorrentino *2018-07-09 17:06:21*

**move to next release**


[b8fb86dc202837c](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8fb86dc202837c) bsorrentino *2018-07-09 17:05:59*

**issue #156 - use home page title as child prefix**


[170fb4adba031fe](https://github.com/bsorrentino/maven-confluence-plugin/commit/170fb4adba031fe) bsorrentino *2018-07-06 11:05:48*

**move to next developer version**


[dbed3061afa6abd](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbed3061afa6abd) bsorrentino *2018-07-04 11:04:57*

**Merge branch 'release/6.0-beta1' into develop**


[20f07c47be14dba](https://github.com/bsorrentino/maven-confluence-plugin/commit/20f07c47be14dba) bsorrentino *2018-07-04 10:58:29*


## v6.0-beta1
### Generic changes

**Merge branch 'release/6.0-beta1'**


[be02969cf1716af](https://github.com/bsorrentino/maven-confluence-plugin/commit/be02969cf1716af) bsorrentino *2018-07-04 10:58:20*

**prepare for release**


[74f25ae063d79ec](https://github.com/bsorrentino/maven-confluence-plugin/commit/74f25ae063d79ec) bsorrentino *2018-07-04 10:36:50*

**update**


[b8bdb6eab03f5e9](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8bdb6eab03f5e9) bsorrentino *2018-06-18 16:10:16*

**Merge remote-tracking branch 'origin/develop' into develop**


[eebc9498febf392](https://github.com/bsorrentino/maven-confluence-plugin/commit/eebc9498febf392) bsorrentino *2018-06-18 16:05:30*

**Merge branch 'feature/delta' into develop**


[6903f4f0c310c43](https://github.com/bsorrentino/maven-confluence-plugin/commit/6903f4f0c310c43) bsorrentino *2018-06-18 11:17:52*

**- #153 getPage(id) return completablefuture**


[556055681f689d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/556055681f689d7) bsorrentino *2018-06-17 20:43:38*

**- #150 print skipped file**


[4efaff707c74402](https://github.com/bsorrentino/maven-confluence-plugin/commit/4efaff707c74402) bsorrentino *2018-06-17 18:57:28*

**- #150 encoding warn moved as debug message**


[9d9d8dcb718499a](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d9d8dcb718499a) bsorrentino *2018-06-17 15:14:10*

**- #150 suppress titlePrefix argument**


[ec228e1b2895acf](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec228e1b2895acf) bsorrentino *2018-06-17 15:08:32*

**- #150 integrate DeployStateManager**


[1c6a98479a2c23f](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c6a98479a2c23f) bsorrentino *2018-06-16 21:44:44*

**add can update check**


[59e5fe6bf072bba](https://github.com/bsorrentino/maven-confluence-plugin/commit/59e5fe6bf072bba) bsorrentino *2018-06-15 07:01:50*

**issue #153 - introduce use of CompletableFuture**


[a16144ca0a7244e](https://github.com/bsorrentino/maven-confluence-plugin/commit/a16144ca0a7244e) bsorrentino *2018-06-10 20:57:31*

**rename package**


[f26a05a34cfe224](https://github.com/bsorrentino/maven-confluence-plugin/commit/f26a05a34cfe224) bsorrentino *2018-06-10 20:53:29*

**add test for issue #150**


[ba254f2340d5e1f](https://github.com/bsorrentino/maven-confluence-plugin/commit/ba254f2340d5e1f) bsorrentino *2018-06-07 19:01:51*

**add test for issue #150**


[7f04e6ca5197b3a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f04e6ca5197b3a) bsorrentino *2018-06-07 19:01:51*

**issue #150 add deployState initialization**


[0cf100c393e8ea9](https://github.com/bsorrentino/maven-confluence-plugin/commit/0cf100c393e8ea9) bsorrentino *2018-06-07 19:01:51*

**issue #150 add toString() and optional attributes**


[5ef0e860cb3e868](https://github.com/bsorrentino/maven-confluence-plugin/commit/5ef0e860cb3e868) bsorrentino *2018-06-07 19:01:42*

**issue #150 add deployState parameter**


[beb7e0b7071f0ee](https://github.com/bsorrentino/maven-confluence-plugin/commit/beb7e0b7071f0ee) bsorrentino *2018-06-07 19:01:28*

**issue #150 remove DeployStateManager from Site**


[17195e4558257f2](https://github.com/bsorrentino/maven-confluence-plugin/commit/17195e4558257f2) bsorrentino *2018-06-03 17:14:46*

**#150 - logging refinements**


[450c3a9c5e6eda0](https://github.com/bsorrentino/maven-confluence-plugin/commit/450c3a9c5e6eda0) bsorrentino *2018-05-29 17:50:02*

**#150 - rename argument**


[b822fb2ceeff0f7](https://github.com/bsorrentino/maven-confluence-plugin/commit/b822fb2ceeff0f7) bsorrentino *2018-05-28 22:25:05*

**#150 - remove optional on state**


[62bbeee243421b5](https://github.com/bsorrentino/maven-confluence-plugin/commit/62bbeee243421b5) bsorrentino *2018-05-28 22:24:50*

**#150 fix invocation to setDeployStateManager**


[77bdcc708035efe](https://github.com/bsorrentino/maven-confluence-plugin/commit/77bdcc708035efe) bsorrentino *2018-05-28 22:24:20*

**issue #150 - add support of 'state' per 'endpoint'**


[ac0f531860f030c](https://github.com/bsorrentino/maven-confluence-plugin/commit/ac0f531860f030c) bsorrentino *2018-05-28 21:41:10*

**update for issue #150**


[c6d7b8c08a35a54](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6d7b8c08a35a54) bsorrentino *2018-04-23 21:08:20*

**add Optional usage - #153**


[5f39d9a48a0fb2b](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f39d9a48a0fb2b) bsorrentino *2018-04-23 21:08:12*

**add Optional usage - #153**


[bee48a86636b8b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/bee48a86636b8b6) bsorrentino *2018-04-23 21:07:39*

**add api.rpc and api.rest properties - #153**


[9da89d23f6ef4f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/9da89d23f6ef4f3) bsorrentino *2018-04-23 21:07:17*

**remove warning - #153**


[d5e18e6a94c26bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5e18e6a94c26bb) bsorrentino *2018-04-23 21:07:09*

**doesn't return null but error - #153**


[26c376a231b2dfd](https://github.com/bsorrentino/maven-confluence-plugin/commit/26c376a231b2dfd) bsorrentino *2018-04-23 21:06:34*

**update exception description - #153**


[af74462e2e9cac4](https://github.com/bsorrentino/maven-confluence-plugin/commit/af74462e2e9cac4) bsorrentino *2018-04-23 21:06:17*

**introduce Optional usage - #153**


[161206bff8f5114](https://github.com/bsorrentino/maven-confluence-plugin/commit/161206bff8f5114) bsorrentino *2018-04-23 21:06:04*

**remove usage of rxjava - #153**

 * use of java8 Stream - #153

[ae84e73701c0c74](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae84e73701c0c74) bsorrentino *2018-04-23 21:05:17*

**add Optional usage - #153**


[248d70d425b641e](https://github.com/bsorrentino/maven-confluence-plugin/commit/248d70d425b641e) bsorrentino *2018-04-23 19:20:30*

**add Optional usage - #153**


[1caeb5265fa9ee1](https://github.com/bsorrentino/maven-confluence-plugin/commit/1caeb5265fa9ee1) bsorrentino *2018-04-23 19:20:06*

**add api.rpc and api.rest properties**


[dbc2ff9a7a21a59](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbc2ff9a7a21a59) bsorrentino *2018-04-15 10:14:09*

**remove warning**


[ebc09e1482f23cc](https://github.com/bsorrentino/maven-confluence-plugin/commit/ebc09e1482f23cc) bsorrentino *2018-04-15 10:13:32*

**doesn't return null but error**


[a6e1db42136aec0](https://github.com/bsorrentino/maven-confluence-plugin/commit/a6e1db42136aec0) bsorrentino *2018-04-15 10:13:20*

**update exception description**


[3084df2d2b720a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/3084df2d2b720a6) bsorrentino *2018-04-15 10:12:09*

**introduce Optional usage**


[98e0ede2c3ead5d](https://github.com/bsorrentino/maven-confluence-plugin/commit/98e0ede2c3ead5d) bsorrentino *2018-04-10 18:16:07*

**remove usage of rxjava**

 * use of java8 Stream

[744b9890e699821](https://github.com/bsorrentino/maven-confluence-plugin/commit/744b9890e699821) bsorrentino *2018-04-09 23:24:33*

**Merge branch 'hotfix/doc' into develop**


[3f46874fd5fa094](https://github.com/bsorrentino/maven-confluence-plugin/commit/3f46874fd5fa094) bsorrentino *2018-02-24 19:49:56*

**Merge branch 'hotfix/doc'**


[880347d43906ba0](https://github.com/bsorrentino/maven-confluence-plugin/commit/880347d43906ba0) bsorrentino *2018-02-24 19:49:54*

**update doc**


[dfebbee51d377fd](https://github.com/bsorrentino/maven-confluence-plugin/commit/dfebbee51d377fd) bsorrentino *2018-02-24 19:49:32*

**update forge module**


[72d231e3d017d1e](https://github.com/bsorrentino/maven-confluence-plugin/commit/72d231e3d017d1e) bsorrentino *2018-02-24 19:48:40*

**update forge module**


[661c6d111aa758a](https://github.com/bsorrentino/maven-confluence-plugin/commit/661c6d111aa758a) bsorrentino *2018-02-24 19:46:53*

**Merge branch 'hotfix/docs' into develop**


[5732e0b4ab61668](https://github.com/bsorrentino/maven-confluence-plugin/commit/5732e0b4ab61668) bsorrentino *2018-01-08 15:36:20*

**Merge branch 'hotfix/docs'**


[4aca3aefccdcddb](https://github.com/bsorrentino/maven-confluence-plugin/commit/4aca3aefccdcddb) bsorrentino *2018-01-08 15:36:18*

**update docs**


[afcf625b3188359](https://github.com/bsorrentino/maven-confluence-plugin/commit/afcf625b3188359) bsorrentino *2018-01-08 15:35:02*

**update docs**


[6d235639c682334](https://github.com/bsorrentino/maven-confluence-plugin/commit/6d235639c682334) bsorrentino *2018-01-08 15:34:20*

**update docs**


[c5e53272248b9d9](https://github.com/bsorrentino/maven-confluence-plugin/commit/c5e53272248b9d9) bsorrentino *2018-01-08 15:33:26*

**update docs**


[438604c9de8f000](https://github.com/bsorrentino/maven-confluence-plugin/commit/438604c9de8f000) bsorrentino *2018-01-08 15:31:57*

**update docs**


[c961f67ed67c9f9](https://github.com/bsorrentino/maven-confluence-plugin/commit/c961f67ed67c9f9) bsorrentino *2018-01-08 15:25:00*

**disable dependencyLocations on site generation**


[3cdbf492f337b22](https://github.com/bsorrentino/maven-confluence-plugin/commit/3cdbf492f337b22) bsorrentino *2018-01-08 15:19:26*

**update docs**


[d7d0114b087a0b1](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7d0114b087a0b1) bsorrentino *2018-01-08 15:18:48*

**update docs**


[f67cd56d9f1bc10](https://github.com/bsorrentino/maven-confluence-plugin/commit/f67cd56d9f1bc10) bsorrentino *2018-01-08 14:56:32*

**update docs**


[eaaa3c7b757e972](https://github.com/bsorrentino/maven-confluence-plugin/commit/eaaa3c7b757e972) bsorrentino *2018-01-08 14:55:04*

**update readme**


[6cfba6f8a4bc95b](https://github.com/bsorrentino/maven-confluence-plugin/commit/6cfba6f8a4bc95b) bsorrentino *2018-01-08 14:49:46*

**Merge branch 'release/5.1.1' into develop**


[e5e572156e847ee](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5e572156e847ee) bsorrentino *2018-01-04 14:58:08*


## v5.1.1
### Generic changes

**Merge branch 'release/5.1.1'**


[9fbe6068899d591](https://github.com/bsorrentino/maven-confluence-plugin/commit/9fbe6068899d591) bsorrentino *2018-01-04 14:57:58*

**new release**


[b8e97d0e1a81764](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8e97d0e1a81764) bsorrentino *2018-01-04 14:55:10*

**Merge branch 'feature/issue#148' into develop**


[7a27377e638f478](https://github.com/bsorrentino/maven-confluence-plugin/commit/7a27377e638f478) bsorrentino *2018-01-04 10:49:31*

**fix #148**


[b52fc820bebd01a](https://github.com/bsorrentino/maven-confluence-plugin/commit/b52fc820bebd01a) bsorrentino *2018-01-03 18:47:39*

**#148**


[e6b7ef5b0b3b826](https://github.com/bsorrentino/maven-confluence-plugin/commit/e6b7ef5b0b3b826) bsorrentino *2017-12-21 22:12:11*

**update submodule**


[ef97b2c3c9f5afe](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef97b2c3c9f5afe) bsorrentino *2017-11-30 20:39:24*

**update submodules**


[12eaa1f7f232d01](https://github.com/bsorrentino/maven-confluence-plugin/commit/12eaa1f7f232d01) bsorrentino *2017-11-30 16:41:21*

**Merge branch 'release/5.1' into develop**


[6fbfe4c639b5cb9](https://github.com/bsorrentino/maven-confluence-plugin/commit/6fbfe4c639b5cb9) bsorrentino *2017-11-29 19:35:14*


## v5.1
### Generic changes

**Merge branch 'release/5.1'**


[e95a4a8af7aa3d6](https://github.com/bsorrentino/maven-confluence-plugin/commit/e95a4a8af7aa3d6) bsorrentino *2017-11-29 19:35:03*

**new release**


[2acc24aee73b174](https://github.com/bsorrentino/maven-confluence-plugin/commit/2acc24aee73b174) bsorrentino *2017-11-29 19:34:25*

**add skip execution #147**


[b8f39aa1bfff9bf](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8f39aa1bfff9bf) bsorrentino *2017-11-28 16:28:45*

**Merge branch 'release/5.0' into develop**


[ae9f9a17877c24f](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae9f9a17877c24f) bsorrentino *2017-08-24 10:18:29*

**fix #96 - complete REST API implementation**


[2aa60fb825db4cf](https://github.com/bsorrentino/maven-confluence-plugin/commit/2aa60fb825db4cf) bsorrentino *2017-08-14 12:43:26*

**implement exportPage**


[bbc8501105504ee](https://github.com/bsorrentino/maven-confluence-plugin/commit/bbc8501105504ee) bsorrentino *2017-08-14 12:38:29*

**create profile for integration test**


[504644d4250648f](https://github.com/bsorrentino/maven-confluence-plugin/commit/504644d4250648f) bsorrentino *2017-08-14 12:24:03*

**add rest support for label and attachments**


[a5d56eae1812837](https://github.com/bsorrentino/maven-confluence-plugin/commit/a5d56eae1812837) bsorrentino *2017-08-13 20:47:18*

**add retorlamda plugin**


[9e7ffcdbd66aa1d](https://github.com/bsorrentino/maven-confluence-plugin/commit/9e7ffcdbd66aa1d) bsorrentino *2017-08-13 11:15:23*

**fix #141**


[17acea20ebb27bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/17acea20ebb27bb) bsorrentino *2017-08-12 21:19:39*

**update readme**


[6dc9fed4ca5472c](https://github.com/bsorrentino/maven-confluence-plugin/commit/6dc9fed4ca5472c) bsorrentino *2017-05-01 14:07:54*

**fix #138**


[870d0c7cd4b1bda](https://github.com/bsorrentino/maven-confluence-plugin/commit/870d0c7cd4b1bda) bsorrentino *2017-05-01 14:04:16*

**update readme**


[55625f4c4f49c9a](https://github.com/bsorrentino/maven-confluence-plugin/commit/55625f4c4f49c9a) bsorrentino *2017-04-26 20:59:10*

**move to next development version**


[08026d6f148c88e](https://github.com/bsorrentino/maven-confluence-plugin/commit/08026d6f148c88e) bsorrentino *2017-04-26 20:58:04*

**Merge branch 'release/5.0-rc5' into develop**


[a8235e447d221d2](https://github.com/bsorrentino/maven-confluence-plugin/commit/a8235e447d221d2) bsorrentino *2017-04-26 20:52:00*


## v5.0
### Generic changes

**Merge branch 'release/5.0'**


[43586baf28fd036](https://github.com/bsorrentino/maven-confluence-plugin/commit/43586baf28fd036) bsorrentino *2017-08-24 10:18:17*

**update javadoc**


[a960b301c146a79](https://github.com/bsorrentino/maven-confluence-plugin/commit/a960b301c146a79) bsorrentino *2017-08-24 10:17:48*

**update project version and documentation**


[dcd180fc2d391d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/dcd180fc2d391d4) bsorrentino *2017-08-24 10:17:30*

**update README**


[9b8d247bbf9b6d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/9b8d247bbf9b6d7) bsorrentino *2017-08-22 08:48:08*

**update README**


[59bb1ee4bd24272](https://github.com/bsorrentino/maven-confluence-plugin/commit/59bb1ee4bd24272) bsorrentino *2017-08-22 08:46:56*

**fix rest endpoint**


[247cb8168ad546f](https://github.com/bsorrentino/maven-confluence-plugin/commit/247cb8168ad546f) bsorrentino *2017-08-21 22:17:18*

**remove duplicate version**


[697ca5f49f629aa](https://github.com/bsorrentino/maven-confluence-plugin/commit/697ca5f49f629aa) bsorrentino *2017-08-21 22:16:48*

**update README**


[d404378c5ff0f29](https://github.com/bsorrentino/maven-confluence-plugin/commit/d404378c5ff0f29) bsorrentino *2017-08-21 22:16:18*

**verified fix #142**


[067be8f4b12124d](https://github.com/bsorrentino/maven-confluence-plugin/commit/067be8f4b12124d) bsorrentino *2017-08-21 22:15:48*

**update README**


[c97c647e7ea3231](https://github.com/bsorrentino/maven-confluence-plugin/commit/c97c647e7ea3231) bsorrentino *2017-08-21 10:08:09*

**update README**


[ec7b32614cb307a](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec7b32614cb307a) bsorrentino *2017-08-21 10:07:34*

**update README**


[1484c48126964ea](https://github.com/bsorrentino/maven-confluence-plugin/commit/1484c48126964ea) bsorrentino *2017-08-21 10:06:30*

**update README**


[c3a6be564aae70e](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3a6be564aae70e) bsorrentino *2017-08-21 10:05:00*

**#96 add full support of rest api**


[6f01eb685711084](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f01eb685711084) bsorrentino *2017-08-21 09:52:04*

**minor refactoring**


[68b51d070fd3589](https://github.com/bsorrentino/maven-confluence-plugin/commit/68b51d070fd3589) bsorrentino *2017-08-18 16:54:45*

**remove deprecated class**


[420a65cbd761f8b](https://github.com/bsorrentino/maven-confluence-plugin/commit/420a65cbd761f8b) bsorrentino *2017-08-18 16:53:45*

**#142 - remove cast on add attachment**


[d7ade3799bf2165](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7ade3799bf2165) bsorrentino *2017-08-17 23:36:20*


## v5.0-rc5
### Generic changes

**Merge branch 'release/5.0-rc5'**


[bb9571711e42372](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb9571711e42372) bsorrentino *2017-04-26 20:51:53*

**move to next version**


[644b3c06593c2a4](https://github.com/bsorrentino/maven-confluence-plugin/commit/644b3c06593c2a4) bsorrentino *2017-04-26 20:51:40*

**fix issue #108**


[4e05072a815c38d](https://github.com/bsorrentino/maven-confluence-plugin/commit/4e05072a815c38d) bsorrentino *2017-04-22 14:00:54*

**update doc**


[58db33b7b426509](https://github.com/bsorrentino/maven-confluence-plugin/commit/58db33b7b426509) bsorrentino *2017-04-20 20:50:04*

**merge from hotfix**


[8dafe3124e65d5e](https://github.com/bsorrentino/maven-confluence-plugin/commit/8dafe3124e65d5e) bsorrentino *2017-04-20 20:41:09*

**Merge branch 'hotfix/readme'**


[aed2b43d9c6a6d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/aed2b43d9c6a6d5) bsorrentino *2017-04-20 20:40:24*

**update readme**


[09fd1a22fce54c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/09fd1a22fce54c1) bsorrentino *2017-04-20 20:40:14*

**move to next dev release**


[5a6fd4a4eea4e65](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a6fd4a4eea4e65) bsorrentino *2017-04-20 20:39:19*

**Merge branch 'release/5.0-rc4' into develop**


[a78efe70e40fa12](https://github.com/bsorrentino/maven-confluence-plugin/commit/a78efe70e40fa12) bsorrentino *2017-04-20 20:34:02*


## v5.0-rc4
### Generic changes

**Merge branch 'release/5.0-rc4'**


[c9db7295683e242](https://github.com/bsorrentino/maven-confluence-plugin/commit/c9db7295683e242) bsorrentino *2017-04-20 20:33:54*

**update doc**


[e60ed143efdd754](https://github.com/bsorrentino/maven-confluence-plugin/commit/e60ed143efdd754) bsorrentino *2017-04-20 20:33:30*

**update doc**


[75755aa8b26e8b4](https://github.com/bsorrentino/maven-confluence-plugin/commit/75755aa8b26e8b4) bsorrentino *2017-04-20 20:28:27*

**update version and docs**


[b104f67e31825da](https://github.com/bsorrentino/maven-confluence-plugin/commit/b104f67e31825da) bsorrentino *2017-04-20 20:17:31*

**Merge branch 'feature/pull#137' into develop**


[26079fe090e23a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/26079fe090e23a5) bsorrentino *2017-04-20 19:50:41*

**add support for  DirectoryStream<Path>**


[211548b71227ca2](https://github.com/bsorrentino/maven-confluence-plugin/commit/211548b71227ca2) bsorrentino *2017-04-20 19:49:11*

**merge pull #137**


[7fce099772aa1e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/7fce099772aa1e2) bsorrentino *2017-04-20 12:35:13*

**Merge branch 'master-clean' of https://github.com/jprafael/maven-confluence-plugin into feature/pull#137**


[268e24f12a2ef39](https://github.com/bsorrentino/maven-confluence-plugin/commit/268e24f12a2ef39) bsorrentino *2017-04-20 12:23:30*

**Allow for attachments to include all files in a directory**


[50cb0d0f997c805](https://github.com/bsorrentino/maven-confluence-plugin/commit/50cb0d0f997c805) João Rafael *2017-04-12 10:54:30*

**update forge module**


[9d76803aa9efa6a](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d76803aa9efa6a) bsorrentino *2017-02-23 16:11:04*

**move to next developer release**


[eb44db543a06762](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb44db543a06762) bsorrentino *2017-02-20 17:12:00*

**Merge branch 'release/5.0-rc3' into develop**


[7b7cd0d99a8273d](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b7cd0d99a8273d) bsorrentino *2017-02-20 17:01:42*


## v5.0-rc3
### Generic changes

**Merge branch 'release/5.0-rc3'**


[5df7a5275f948e0](https://github.com/bsorrentino/maven-confluence-plugin/commit/5df7a5275f948e0) bsorrentino *2017-02-20 17:01:22*

**prepare for release 5.0-rc3**


[173a20540747ab2](https://github.com/bsorrentino/maven-confluence-plugin/commit/173a20540747ab2) bsorrentino *2017-02-20 17:00:57*

**add integration test profiles 'itest' 'itst-core'**


[69dc8afb6a4a23a](https://github.com/bsorrentino/maven-confluence-plugin/commit/69dc8afb6a4a23a) bsorrentino *2017-02-18 16:19:20*

**Merge branch 'develop' into wattazoum-118-pluginsGoalsAschild-5.0**


[12a715aba5fb52a](https://github.com/bsorrentino/maven-confluence-plugin/commit/12a715aba5fb52a) bsorrentino *2017-02-18 15:25:20*

**update guide**


[c6024a12bece9ae](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6024a12bece9ae) bsorrentino *2017-02-18 15:22:05*

**add project to test the plugin documentation against a live confluence instance**

 * generated fresh schema named &#x27;site-schema-5.0&#x27;

[3cdb9f9f1ebe839](https://github.com/bsorrentino/maven-confluence-plugin/commit/3cdb9f9f1ebe839) bsorrentino *2017-02-18 15:06:01*

**remove unused import**


[6ea11e456a25ad6](https://github.com/bsorrentino/maven-confluence-plugin/commit/6ea11e456a25ad6) bsorrentino *2017-02-18 14:14:09*

**fix schema generation + rename author from javadoc**


[f89d45d58cfe31e](https://github.com/bsorrentino/maven-confluence-plugin/commit/f89d45d58cfe31e) bsorrentino *2017-02-18 13:18:27*

**update version, fix schema generation**


[0a5a0c6b956ea8a](https://github.com/bsorrentino/maven-confluence-plugin/commit/0a5a0c6b956ea8a) bsorrentino *2017-02-16 23:09:23*

**Merge branch '118-goalsAsChildren' into 118-pluginsGoalsAschild-5.0**


[54ac88fac82e23b](https://github.com/bsorrentino/maven-confluence-plugin/commit/54ac88fac82e23b) wattazoum *2017-02-15 19:56:15*

**Keep the old title of the goal pages**


[8f3a1831b954cbe](https://github.com/bsorrentino/maven-confluence-plugin/commit/8f3a1831b954cbe) wattazoum *2017-02-14 23:50:44*

**Generate the plugins goal under a specific page**


[244e6a3f1ec6c07](https://github.com/bsorrentino/maven-confluence-plugin/commit/244e6a3f1ec6c07) wattazoum *2017-02-14 23:31:48*

**update**


[fea41cb73078915](https://github.com/bsorrentino/maven-confluence-plugin/commit/fea41cb73078915) bsorrentino *2017-02-11 14:09:51*

**Merge branch 'hotfix/doc' into develop**


[a4c98d5f35a4d01](https://github.com/bsorrentino/maven-confluence-plugin/commit/a4c98d5f35a4d01) bsorrentino *2017-02-11 14:07:08*

**Merge branch 'hotfix/doc'**


[8e18e952a1fd8e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/8e18e952a1fd8e6) bsorrentino *2017-02-11 14:07:08*

**update**


[959f605c558efbf](https://github.com/bsorrentino/maven-confluence-plugin/commit/959f605c558efbf) bsorrentino *2017-02-11 14:06:53*

**fix year**


[6601ec76e09175a](https://github.com/bsorrentino/maven-confluence-plugin/commit/6601ec76e09175a) bsorrentino *2017-01-18 18:02:58*

**fix year**


[a951b264efeb98a](https://github.com/bsorrentino/maven-confluence-plugin/commit/a951b264efeb98a) bsorrentino *2017-01-18 18:02:35*

**release 5.0-rc2**


[d6fd0e6befd1d3d](https://github.com/bsorrentino/maven-confluence-plugin/commit/d6fd0e6befd1d3d) bsorrentino *2017-01-18 17:58:45*

**endpoint processing refinements**


[bb2465189f393eb](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb2465189f393eb) bsorrentino *2017-01-18 17:58:45*

**update plugin usage doc**


[9cb58bbd0bd55b8](https://github.com/bsorrentino/maven-confluence-plugin/commit/9cb58bbd0bd55b8) bsorrentino *2017-01-18 17:58:45*

**test and fix for issue #133**


[8917204534fa173](https://github.com/bsorrentino/maven-confluence-plugin/commit/8917204534fa173) bsorrentino *2017-01-18 17:58:38*

**test for issue #131**


[d5f6ce9a4d403ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/d5f6ce9a4d403ac) bsorrentino *2017-01-18 17:58:31*

**update plugin usage doc**


[a9043d162677950](https://github.com/bsorrentino/maven-confluence-plugin/commit/a9043d162677950) bsorrentino *2017-01-18 17:57:06*

**release 5.0-rc2**


[677d870ef017d8f](https://github.com/bsorrentino/maven-confluence-plugin/commit/677d870ef017d8f) bsorrentino *2017-01-18 17:57:06*

**move to next release**


[1e01fbc231fa2b2](https://github.com/bsorrentino/maven-confluence-plugin/commit/1e01fbc231fa2b2) bsorrentino *2017-01-18 17:57:06*

**endpoint processing refinements**


[083e154127f38e3](https://github.com/bsorrentino/maven-confluence-plugin/commit/083e154127f38e3) bsorrentino *2017-01-18 17:57:06*

**test and fix for issue #133**


[8ddef76107bc786](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ddef76107bc786) bsorrentino *2017-01-18 17:56:58*

**test for issue #131**


[5821d56edbf7c14](https://github.com/bsorrentino/maven-confluence-plugin/commit/5821d56edbf7c14) bsorrentino *2017-01-18 17:56:48*

**move to next version**


[dbde3c7ae871a95](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbde3c7ae871a95) bsorrentino *2016-12-06 19:20:00*

**Merge branch 'hotfix/doc' into develop**


[16e9f1494d7533c](https://github.com/bsorrentino/maven-confluence-plugin/commit/16e9f1494d7533c) bsorrentino *2016-12-06 19:09:21*

**update readme**


[de99e41688c7766](https://github.com/bsorrentino/maven-confluence-plugin/commit/de99e41688c7766) bsorrentino *2016-12-06 19:07:12*

**merge**


[9595919c15d4c2c](https://github.com/bsorrentino/maven-confluence-plugin/commit/9595919c15d4c2c) bsorrentino *2016-12-06 18:58:37*

**update doc/javadoc, version**


[094a9ca4fd90012](https://github.com/bsorrentino/maven-confluence-plugin/commit/094a9ca4fd90012) bsorrentino *2016-12-06 16:29:44*

**Merge branch 'feature/issue110' into develop**


[a8ace1515716429](https://github.com/bsorrentino/maven-confluence-plugin/commit/a8ace1515716429) bsorrentino *2016-12-06 15:43:34*

**finalize implementation of issue110**


[38db79fc0cc636a](https://github.com/bsorrentino/maven-confluence-plugin/commit/38db79fc0cc636a) bsorrentino *2016-12-06 00:23:09*

**add exceptions management**


[ddbc15d325c1c10](https://github.com/bsorrentino/maven-confluence-plugin/commit/ddbc15d325c1c10) bsorrentino *2016-12-05 14:52:25*

**update readme**


[44af7fd10ac98c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/44af7fd10ac98c2) bsorrentino *2016-11-22 23:45:31*

**fix parent version**


[eb61117e42cd0c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb61117e42cd0c5) bsorrentino *2016-11-22 23:40:29*

**updating poms for 55.0-SNAPSHOT development**


[cdb4360bbdf0439](https://github.com/bsorrentino/maven-confluence-plugin/commit/cdb4360bbdf0439) bsorrentino *2016-11-22 22:49:57*

**separate itests**


[32db30877d80d7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/32db30877d80d7b) bsorrentino *2016-11-22 22:46:01*

**Merge branch 'feature/issue#130' into develop**


[905864549d7ae01](https://github.com/bsorrentino/maven-confluence-plugin/commit/905864549d7ae01) bsorrentino *2016-11-22 22:39:29*

**fix unit & integration tests**


[19246a40ab9c562](https://github.com/bsorrentino/maven-confluence-plugin/commit/19246a40ab9c562) bsorrentino *2016-11-22 22:39:07*

**remove useless stuff**


[3a25ad958b73b1a](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a25ad958b73b1a) bsorrentino *2016-11-19 20:38:02*

**update readme**


[8eb0940f72484c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/8eb0940f72484c3) bsorrentino *2016-11-19 19:58:34*

**add support for SSL for REST**


[6b533dbd5b3530b](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b533dbd5b3530b) bsorrentino *2016-11-19 19:46:39*

**move model classes from plugin to core module**


[f5e725b392e5e79](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5e725b392e5e79) bsorrentino *2016-10-23 14:20:27*

**add inline documentation**


[0666a5c84972de7](https://github.com/bsorrentino/maven-confluence-plugin/commit/0666a5c84972de7) bsorrentino *2016-09-28 10:35:32*

**issue#129**


[2b3b59bc161bfba](https://github.com/bsorrentino/maven-confluence-plugin/commit/2b3b59bc161bfba) bsorrentino *2016-09-28 10:09:35*

**update documentation**


[f0eaf2daaf148b4](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0eaf2daaf148b4) bsorrentino *2016-09-28 10:06:59*

**update documentation**


[7f622d679e1a433](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f622d679e1a433) bsorrentino *2016-09-28 10:05:18*

**update documentation**


[49f3a77eff2e0d3](https://github.com/bsorrentino/maven-confluence-plugin/commit/49f3a77eff2e0d3) bsorrentino *2016-09-24 10:22:22*

**Merge branch 'master' into develop**


[bca8499202cf21e](https://github.com/bsorrentino/maven-confluence-plugin/commit/bca8499202cf21e) bsorrentino *2016-09-24 10:04:35*

**Updating develop poms back to pre merge state**


[483204ea76c4f70](https://github.com/bsorrentino/maven-confluence-plugin/commit/483204ea76c4f70) bsorrentino *2016-09-24 10:04:35*

**updating develop poms to master versions to avoid merge conflicts**


[0071d1701033eca](https://github.com/bsorrentino/maven-confluence-plugin/commit/0071d1701033eca) bsorrentino *2016-09-24 10:04:34*

**Add some integration testing to test #124**


[5fb9c6e9ebdcf8b](https://github.com/bsorrentino/maven-confluence-plugin/commit/5fb9c6e9ebdcf8b) wattazoum *2016-09-04 07:25:27*

**fix issue 124 - to test**


[3f8d45146585e01](https://github.com/bsorrentino/maven-confluence-plugin/commit/3f8d45146585e01) bsorrentino *2016-09-02 12:29:20*

**update README**


[eeafbae3992f0bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/eeafbae3992f0bb) bsorrentino *2016-08-26 09:16:13*

**Merge branch 'wattazoum-issue-122' into support/4.x**


[a0bbd9dc835659f](https://github.com/bsorrentino/maven-confluence-plugin/commit/a0bbd9dc835659f) bsorrentino *2016-08-26 09:06:42*

**Add some line breaks (fixes #122)**


[371e55020e8f1c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/371e55020e8f1c5) wattazoum *2016-08-15 12:27:28*

**move to next version**


[955630bcd5cef03](https://github.com/bsorrentino/maven-confluence-plugin/commit/955630bcd5cef03) bsorrentino *2016-08-11 10:41:23*

**Merge branch 'master' into support/4.x**


[22d65514d507510](https://github.com/bsorrentino/maven-confluence-plugin/commit/22d65514d507510) bsorrentino *2016-08-11 10:38:20*

**prepare for release**


[d216eaac443eb7c](https://github.com/bsorrentino/maven-confluence-plugin/commit/d216eaac443eb7c) bsorrentino *2016-08-11 09:44:47*

**update README**


[f55f652ae5669d0](https://github.com/bsorrentino/maven-confluence-plugin/commit/f55f652ae5669d0) bsorrentino *2016-08-11 09:35:26*

**update README**


[6e3b500e60aeb33](https://github.com/bsorrentino/maven-confluence-plugin/commit/6e3b500e60aeb33) bsorrentino *2016-08-11 09:27:28*

**update README**


[964c3a7c66e5ad6](https://github.com/bsorrentino/maven-confluence-plugin/commit/964c3a7c66e5ad6) bsorrentino *2016-08-11 09:26:53*

**update README**


[acf610527e98e24](https://github.com/bsorrentino/maven-confluence-plugin/commit/acf610527e98e24) bsorrentino *2016-08-11 09:22:52*

**update README**


[4ad228d8c0c567b](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ad228d8c0c567b) bsorrentino *2016-08-11 09:20:16*

**Merge branch 'wattazoum-goals-pages-styling' into support/4.x**


[3d44336b9379a97](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d44336b9379a97) bsorrentino *2016-08-11 09:18:37*


## v5.0-beta2
### Generic changes

**merge new release**


[807e10ef1a043ba](https://github.com/bsorrentino/maven-confluence-plugin/commit/807e10ef1a043ba) bsorrentino *2016-11-22 22:58:03*

**updating poms for branch'release/5.0-beta2' with non-snapshot versions**


[0dfcfa7fe46e4a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/0dfcfa7fe46e4a7) bsorrentino *2016-11-22 22:54:53*

**update doc**


[6eda9c5911a6d12](https://github.com/bsorrentino/maven-confluence-plugin/commit/6eda9c5911a6d12) bsorrentino *2016-11-22 22:54:01*

**updating poms for 5.0-beta2 branch with snapshot versions**


[31373adc564518b](https://github.com/bsorrentino/maven-confluence-plugin/commit/31373adc564518b) bsorrentino *2016-11-22 22:49:27*

**update documentation**


[e78206ff8b90974](https://github.com/bsorrentino/maven-confluence-plugin/commit/e78206ff8b90974) bsorrentino *2016-09-24 10:11:10*


## v5.0-beta1
### Generic changes

**Merge branch 'release/5.0-beta1'**


[10355e1e9924856](https://github.com/bsorrentino/maven-confluence-plugin/commit/10355e1e9924856) bsorrentino *2016-09-24 10:04:32*

**updating poms for branch'release/5.0-beta1' with non-snapshot versions**


[3d45c3f9949b7af](https://github.com/bsorrentino/maven-confluence-plugin/commit/3d45c3f9949b7af) bsorrentino *2016-09-24 10:04:08*

**update documentation**


[7dcf3d755851ea6](https://github.com/bsorrentino/maven-confluence-plugin/commit/7dcf3d755851ea6) bsorrentino *2016-09-24 10:00:47*

**updating poms for 5.0-beta1 branch with snapshot versions**


[9ff5805a4ad862b](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ff5805a4ad862b) bsorrentino *2016-09-23 22:43:42*

**fix merge with issue#124**


[8d1bdd7aee76082](https://github.com/bsorrentino/maven-confluence-plugin/commit/8d1bdd7aee76082) bsorrentino *2016-09-05 16:40:22*

**merged with 4.13 version from master**


[2fd2624ad98eba1](https://github.com/bsorrentino/maven-confluence-plugin/commit/2fd2624ad98eba1) bsorrentino *2016-09-05 16:22:28*

**Merge branch 'feature/issue#122' into develop**


[0f36d05c7697a36](https://github.com/bsorrentino/maven-confluence-plugin/commit/0f36d05c7697a36) bsorrentino *2016-08-26 09:35:04*

**resolve conflicts**


[8d0a32052b98f8c](https://github.com/bsorrentino/maven-confluence-plugin/commit/8d0a32052b98f8c) bsorrentino *2016-08-26 09:34:45*

**merge documentation from master(4.12)**


[a08a962164293f6](https://github.com/bsorrentino/maven-confluence-plugin/commit/a08a962164293f6) bsorrentino *2016-08-11 10:45:29*

**merged PR#121**


[a71f83770fbda99](https://github.com/bsorrentino/maven-confluence-plugin/commit/a71f83770fbda99) bsorrentino *2016-08-11 09:38:03*

*** add anchors to parameters details**

 * vertical table header for parameters details

[fa9bfb2c5f67b37](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa9bfb2c5f67b37) wattazoum *2016-08-10 20:33:25*

**add test page for PR113**


[31ceda7e16237f1](https://github.com/bsorrentino/maven-confluence-plugin/commit/31ceda7e16237f1) bsorrentino *2016-08-09 13:24:54*

**add test page for PR113**


[1b164f230e69b00](https://github.com/bsorrentino/maven-confluence-plugin/commit/1b164f230e69b00) bsorrentino *2016-08-09 13:22:40*

**Merge branch 'feature/pr#115' into develop**


[5e01ad93c42eb9f](https://github.com/bsorrentino/maven-confluence-plugin/commit/5e01ad93c42eb9f) bsorrentino *2016-08-09 09:14:50*

**merge PR#115**


[604345e6bfd9b26](https://github.com/bsorrentino/maven-confluence-plugin/commit/604345e6bfd9b26) bsorrentino *2016-08-09 09:14:31*

**update README**


[aba22affa0133bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/aba22affa0133bd) bsorrentino *2016-08-09 08:38:32*

**Merge branch 'wattazoum-plugin-doc-more-tags' into support/4.x**


[5a21f36c3514853](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a21f36c3514853) bsorrentino *2016-08-09 08:38:11*

**Merge branch 'plugin-doc-more-tags' of https://github.com/wattazoum/maven-confluence-plugin into wattazoum-plugin-doc-more-tags**


[af5e6c221dccb16](https://github.com/bsorrentino/maven-confluence-plugin/commit/af5e6c221dccb16) bsorrentino *2016-08-09 08:19:36*

**Merge branch 'feature/pr#113' into develop**


[ba131aa4e22a5c4](https://github.com/bsorrentino/maven-confluence-plugin/commit/ba131aa4e22a5c4) bsorrentino *2016-08-09 08:16:57*

**Merge branch 'wattazoum-more-flexibility-to-panels' into feature/pr#113**


[9d7ad8f92bf1890](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d7ad8f92bf1890) bsorrentino *2016-08-09 08:15:29*

**update readme**


[0bc0386e5246788](https://github.com/bsorrentino/maven-confluence-plugin/commit/0bc0386e5246788) bsorrentino *2016-08-08 22:12:11*

**Merge branch 'wattazoum-more-flexibility-to-panels' into support/4.x**

 * t push origin support/4.x

[954212b8d97bd4f](https://github.com/bsorrentino/maven-confluence-plugin/commit/954212b8d97bd4f) bsorrentino *2016-08-08 22:08:15*

**Merge branch 'more-flexibility-to-panels' of https://github.com/wattazoum/maven-confluence-plugin into wattazoum-more-flexibility-to-panels**

 * # fix Conflicts:
 * #	maven-confluence-reporting-plugin/src/test/java/org/bsc/maven/reporting/model/SiteTest.java

[f028be945a81dae](https://github.com/bsorrentino/maven-confluence-plugin/commit/f028be945a81dae) bsorrentino *2016-08-08 22:07:50*

**Merge branch 'feature/issue#119' into develop**


[739b6be4c7d74a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/739b6be4c7d74a6) bsorrentino *2016-08-08 21:57:44*

**Merge branch 'wattazoum-support-for-simplenode' into feature/issue#119**


[eb9cd67736da18f](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb9cd67736da18f) bsorrentino *2016-08-08 21:55:11*

**Merge branch 'wattazoum-support-for-simplenode' into feature/issue#119**


[6f3e947213a659e](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f3e947213a659e) bsorrentino *2016-08-08 21:54:20*

**update README**


[b90704116a81f54](https://github.com/bsorrentino/maven-confluence-plugin/commit/b90704116a81f54) bsorrentino *2016-08-08 21:40:38*

**Merge branch 'wattazoum-support-for-simplenode' into support/4.x**

 * t push origin support/4.x

[9ca258cca6aeaa2](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ca258cca6aeaa2) bsorrentino *2016-08-08 21:33:04*

**Support simpleNode (Horizontal rules + forced line breaks)**


[1d608da808bddb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d608da808bddb2) wattazoum *2016-08-07 22:33:16*

**Merge branch 'upstream/4.x' into plugin-doc-more-tags**

 * # Conflicts:
 * #	maven-confluence-reporting-plugin/src/main/java/org/bsc/maven/confluence/plugin/ConfluenceDeployMojo.java

[02e444a41508034](https://github.com/bsorrentino/maven-confluence-plugin/commit/02e444a41508034) wattazoum *2016-08-07 20:02:56*

**add integration test profile**


[37798d3c0ac23c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/37798d3c0ac23c1) bsorrentino *2016-08-07 18:10:39*

**update README**


[a1594993b383cb7](https://github.com/bsorrentino/maven-confluence-plugin/commit/a1594993b383cb7) bsorrentino *2016-08-07 18:00:05*

**merged PR#117**


[e8fc0d9dfa7b606](https://github.com/bsorrentino/maven-confluence-plugin/commit/e8fc0d9dfa7b606) bsorrentino *2016-08-07 17:56:01*

**Merge branch 'wattazoum-fix-attachements-on-homepage' into support/4.x**


[2a5908f76bad386](https://github.com/bsorrentino/maven-confluence-plugin/commit/2a5908f76bad386) bsorrentino *2016-08-07 17:16:33*

**update README**


[fd2033e245b45c0](https://github.com/bsorrentino/maven-confluence-plugin/commit/fd2033e245b45c0) bsorrentino *2016-08-07 17:15:40*

**Merge branch 'fix-attachements-on-homepage' of https://github.com/wattazoum/maven-confluence-plugin into wattazoum-fix-attachements-on-homepage**


[a7005e585c55db3](https://github.com/bsorrentino/maven-confluence-plugin/commit/a7005e585c55db3) bsorrentino *2016-08-07 17:11:05*

**update README**


[f686e9f4cd0c0ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/f686e9f4cd0c0ab) bsorrentino *2016-08-07 17:10:14*

**resolve conflict after merge issue#114**


[cd7150d5933d1e3](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd7150d5933d1e3) bsorrentino *2016-08-07 16:51:39*

**don't modify the parent page. Add the labels and the attachements to the homepage.**

 * (cherry picked from commit 7d685a3)

[471d1e0ba736dba](https://github.com/bsorrentino/maven-confluence-plugin/commit/471d1e0ba736dba) wattazoum *2016-08-07 00:55:59*

**Warning the user when there is a first upload of attachement is too scary**

 * (cherry picked from commit 06b5b3e)

[e60bbe6720128d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/e60bbe6720128d7) wattazoum *2016-08-07 00:48:28*

*** adding support for some html tags in javadoc. <u> <i> and <s>**

 * escaping confluence reserved chars
 * add some logs

[d63f779fd75c6fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/d63f779fd75c6fb) wattazoum *2016-08-06 22:43:00*

**fix waiting for test**


[96bb83e653ce9b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/96bb83e653ce9b6) bsorrentino *2016-08-05 22:11:51*

**Merge branch 'feature/pr#112' into develop**


[1a64722f77abeda](https://github.com/bsorrentino/maven-confluence-plugin/commit/1a64722f77abeda) bsorrentino *2016-08-05 13:45:54*

**merge from PR112**


[3b3e49f92b4ce0a](https://github.com/bsorrentino/maven-confluence-plugin/commit/3b3e49f92b4ce0a) bsorrentino *2016-08-05 13:45:23*

**add more flexibility to panels syntax**


[026b3d75cb4c696](https://github.com/bsorrentino/maven-confluence-plugin/commit/026b3d75cb4c696) wattazoum *2016-08-05 13:14:11*

**fix issue#111**


[dad53978f03726f](https://github.com/bsorrentino/maven-confluence-plugin/commit/dad53978f03726f) bsorrentino *2016-08-05 12:17:46*

**Merge branch 'feature/issue#108' into develop**


[66aa936b147ae0f](https://github.com/bsorrentino/maven-confluence-plugin/commit/66aa936b147ae0f) bsorrentino *2016-07-28 23:26:24*

**update readme**


[b8c8914508a9b27](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8c8914508a9b27) bsorrentino *2016-07-28 23:26:05*

**fix issue on storage format update**


[f1f67c7f1311826](https://github.com/bsorrentino/maven-confluence-plugin/commit/f1f67c7f1311826) bsorrentino *2016-07-28 23:16:34*

**test refinements**


[bc4d9f89098152c](https://github.com/bsorrentino/maven-confluence-plugin/commit/bc4d9f89098152c) bsorrentino *2016-07-28 20:29:47*

**add support for storage format**


[6aa49385521255b](https://github.com/bsorrentino/maven-confluence-plugin/commit/6aa49385521255b) bsorrentino *2016-07-28 17:18:01*

**update after adding rxjava**


[65d279f826049e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/65d279f826049e2) bsorrentino *2016-07-28 16:40:21*

**add storePage REST service**


[e5e13caa2080b47](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5e13caa2080b47) bsorrentino *2016-07-28 16:39:18*

**add rxjava support for rest api**


[8138d81dad9883b](https://github.com/bsorrentino/maven-confluence-plugin/commit/8138d81dad9883b) bsorrentino *2016-07-26 17:57:47*

**add storage/representation class/enum**


[e52e1bd6ad40609](https://github.com/bsorrentino/maven-confluence-plugin/commit/e52e1bd6ad40609) bsorrentino *2016-07-24 22:42:59*

**service interface refinements**


[7910c4c9017d0fb](https://github.com/bsorrentino/maven-confluence-plugin/commit/7910c4c9017d0fb) bsorrentino *2016-07-24 22:31:55*

**add credentials**


[c1f5714a45ca30d](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1f5714a45ca30d) bsorrentino *2016-07-24 21:51:15*

**save credentials**


[710c441019fcf2d](https://github.com/bsorrentino/maven-confluence-plugin/commit/710c441019fcf2d) bsorrentino *2016-07-24 21:50:41*

**add and remove page integration-test**


[88d975bc615d669](https://github.com/bsorrentino/maven-confluence-plugin/commit/88d975bc615d669) bsorrentino *2016-07-09 22:57:12*

**resolve merge conflicts**


[f38d40a7ec4fd27](https://github.com/bsorrentino/maven-confluence-plugin/commit/f38d40a7ec4fd27) bsorrentino *2016-07-08 21:14:55*

**update**


[b57580b7e912b25](https://github.com/bsorrentino/maven-confluence-plugin/commit/b57580b7e912b25) bsorrentino *2016-07-08 20:49:32*

**change log level for 'variable not defined in template'**


[576bdbcafe4d9b9](https://github.com/bsorrentino/maven-confluence-plugin/commit/576bdbcafe4d9b9) bsorrentino *2016-07-07 20:36:46*

**move to 5.0**


[32d39c2b4ac2626](https://github.com/bsorrentino/maven-confluence-plugin/commit/32d39c2b4ac2626) bsorrentino *2016-07-07 11:03:12*

**update gitflow plugin configuration**


[a404e77e8310c78](https://github.com/bsorrentino/maven-confluence-plugin/commit/a404e77e8310c78) bsorrentino *2016-07-01 09:46:50*

**merged from origin**


[e7aba7ba1be10e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/e7aba7ba1be10e6) bsorrentino *2016-07-01 09:42:02*

**reduce removePage overloading**


[90b17f1941f7135](https://github.com/bsorrentino/maven-confluence-plugin/commit/90b17f1941f7135) bsorrentino *2016-07-01 09:30:20*

**merge from develop**


[de620c41e541930](https://github.com/bsorrentino/maven-confluence-plugin/commit/de620c41e541930) bsorrentino *2016-07-01 09:13:20*

**remove swizzle deps and include sources**


[aeaa0ab095f8c56](https://github.com/bsorrentino/maven-confluence-plugin/commit/aeaa0ab095f8c56) bsorrentino *2016-06-28 08:01:53*

**start refactoring to introduce use of REST Api**


[e3d064bdbd83dc9](https://github.com/bsorrentino/maven-confluence-plugin/commit/e3d064bdbd83dc9) bsorrentino *2016-06-26 07:30:51*

**remove swizzle deps and include sources**


[ebbe2a9c61ce264](https://github.com/bsorrentino/maven-confluence-plugin/commit/ebbe2a9c61ce264) bsorrentino *2016-06-23 12:29:27*

**remove swizzle deps and include sources**


[78d7e5d5f2f297c](https://github.com/bsorrentino/maven-confluence-plugin/commit/78d7e5d5f2f297c) bsorrentino *2016-06-22 14:14:37*


## v4.13
### Generic changes

**Merge branch 'support/4.x'**


[de08d6487ab0865](https://github.com/bsorrentino/maven-confluence-plugin/commit/de08d6487ab0865) bsorrentino *2016-09-05 15:55:36*

**Merge branch 'release/4.13' into support/4.x**


[1c38c4028f887ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c38c4028f887ab) bsorrentino *2016-09-05 15:54:25*

**prepare for release**


[ccf801032a16fff](https://github.com/bsorrentino/maven-confluence-plugin/commit/ccf801032a16fff) bsorrentino *2016-09-05 15:53:14*

**Merge branch 'feature/issue#124' into support/4.x**


[f24fd4bdb82a84b](https://github.com/bsorrentino/maven-confluence-plugin/commit/f24fd4bdb82a84b) bsorrentino *2016-09-05 14:58:06*

**update readme**


[e9c955ad2404dd0](https://github.com/bsorrentino/maven-confluence-plugin/commit/e9c955ad2404dd0) bsorrentino *2016-09-05 14:57:36*


## v4.12
### Generic changes

**update doc**


[7035dcd90517bfb](https://github.com/bsorrentino/maven-confluence-plugin/commit/7035dcd90517bfb) bsorrentino *2016-08-11 10:03:34*

**update README**


[4cc74f7b782054c](https://github.com/bsorrentino/maven-confluence-plugin/commit/4cc74f7b782054c) bsorrentino *2016-08-11 09:48:01*

**Merge branch 'support/4.x'**


[2f8d8a21a411a3a](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f8d8a21a411a3a) bsorrentino *2016-08-11 09:44:51*


## v4.11
### Generic changes

**remove title**


[80f3b634301a7a0](https://github.com/bsorrentino/maven-confluence-plugin/commit/80f3b634301a7a0) bsorrentino *2016-08-05 13:22:14*

**arrange release 4.11**


[1c4d7c81ee21d24](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c4d7c81ee21d24) bsorrentino *2016-08-05 13:09:06*

**Merge branch 'wattazoum-support-referencenode' into support/4.x**


[dcf1cb7a6aa3d79](https://github.com/bsorrentino/maven-confluence-plugin/commit/dcf1cb7a6aa3d79) bsorrentino *2016-08-05 12:51:54*

**update documentation. update test project**


[7627366a34493cc](https://github.com/bsorrentino/maven-confluence-plugin/commit/7627366a34493cc) bsorrentino *2016-08-05 12:50:49*

**Merge branch 'support-referencenode' of https://github.com/wattazoum/maven-confluence-plugin into wattazoum-support-referencenode**


[d7b954849aefee1](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7b954849aefee1) bsorrentino *2016-08-05 12:25:32*

**fix issue#111**


[615bec73a392eb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/615bec73a392eb2) bsorrentino *2016-08-05 12:15:06*

**Support RefImageLink**


[6c9f471f379c30a](https://github.com/bsorrentino/maven-confluence-plugin/commit/6c9f471f379c30a) wattazoum *2016-08-05 08:09:25*

**Ref link support added.**


[2d40f1d60a8a64e](https://github.com/bsorrentino/maven-confluence-plugin/commit/2d40f1d60a8a64e) wattazoum *2016-08-05 00:54:23*

**Merge branch 'master' into develop**


[5b9afd680e3e21e](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b9afd680e3e21e) bsorrentino *2016-07-01 09:57:34*

**Updating develop poms back to pre merge state**


[0d48d67b54b38ce](https://github.com/bsorrentino/maven-confluence-plugin/commit/0d48d67b54b38ce) bsorrentino *2016-07-01 09:57:34*

**updating develop poms to master versions to avoid merge conflicts**


[40b5680ba071fea](https://github.com/bsorrentino/maven-confluence-plugin/commit/40b5680ba071fea) bsorrentino *2016-07-01 09:57:33*

**updating poms for 4.11-SNAPSHOT development**


[a55ac23490c6b69](https://github.com/bsorrentino/maven-confluence-plugin/commit/a55ac23490c6b69) bsorrentino *2016-07-01 09:49:57*

**Merge branch 'feature/issue#107' into develop**


[407a3bf9f87944a](https://github.com/bsorrentino/maven-confluence-plugin/commit/407a3bf9f87944a) bsorrentino *2016-06-26 19:58:56*

**move spaceKey argument to optional**

 * test refinements

[3ff3ae5ea441252](https://github.com/bsorrentino/maven-confluence-plugin/commit/3ff3ae5ea441252) bsorrentino *2016-06-26 19:53:02*

**add parentPageId parameter management**


[74a73b514185fd8](https://github.com/bsorrentino/maven-confluence-plugin/commit/74a73b514185fd8) bsorrentino *2016-06-26 18:21:20*

**update readme**


[c313bd28efb14e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/c313bd28efb14e5) bsorrentino *2016-06-23 09:44:31*

**issue#109 - apply patch, test and update doc**


[4c700b943610fe9](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c700b943610fe9) bsorrentino *2016-06-23 09:39:58*

**Updating develop poms back to pre merge state**


[afa8d3fe85f1600](https://github.com/bsorrentino/maven-confluence-plugin/commit/afa8d3fe85f1600) bsorrentino *2016-06-21 08:40:15*

**Merge branch 'master' into develop**


[138739449ade2dc](https://github.com/bsorrentino/maven-confluence-plugin/commit/138739449ade2dc) bsorrentino *2016-06-21 08:40:15*

**Updating develop poms to hotfix version to avoid merge conflicts**


[9a2a64ad198d06e](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a2a64ad198d06e) bsorrentino *2016-06-21 08:40:14*

**Updating develop poms back to pre merge state**


[9276a6c4dafe7c0](https://github.com/bsorrentino/maven-confluence-plugin/commit/9276a6c4dafe7c0) bsorrentino *2016-06-14 10:31:44*

**Merge branch 'master' into develop**


[e933599d124284d](https://github.com/bsorrentino/maven-confluence-plugin/commit/e933599d124284d) bsorrentino *2016-06-14 10:31:43*

**updating develop poms to master versions to avoid merge conflicts**


[79cf69092ef6d8f](https://github.com/bsorrentino/maven-confluence-plugin/commit/79cf69092ef6d8f) bsorrentino *2016-06-14 10:31:43*

**updating poms for 4.10-SNAPSHOT development**


[034b0029a68e906](https://github.com/bsorrentino/maven-confluence-plugin/commit/034b0029a68e906) bsorrentino *2016-06-14 10:00:47*


## v4.10
### Generic changes

**Merge branch 'release/4.10'**


[eb8ed01506e48bc](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb8ed01506e48bc) bsorrentino *2016-07-01 09:57:31*

**updating poms for branch'release/4.10' with non-snapshot versions**


[9086359fb0b1156](https://github.com/bsorrentino/maven-confluence-plugin/commit/9086359fb0b1156) bsorrentino *2016-07-01 09:56:49*

**update readme, update maven gitflow conf**


[f849d8561771bcc](https://github.com/bsorrentino/maven-confluence-plugin/commit/f849d8561771bcc) bsorrentino *2016-07-01 09:56:05*


## v4.9.1
### Generic changes

**Merge branch 'hotfix/4.9.1'**


[582e2a14a63307c](https://github.com/bsorrentino/maven-confluence-plugin/commit/582e2a14a63307c) bsorrentino *2016-06-21 08:40:13*

**updating poms for branch'hotfix/4.9.1' with non-snapshot versions**


[917205aa76c783f](https://github.com/bsorrentino/maven-confluence-plugin/commit/917205aa76c783f) bsorrentino *2016-06-21 08:37:59*

**update readme**


[d79e178e4b76879](https://github.com/bsorrentino/maven-confluence-plugin/commit/d79e178e4b76879) bsorrentino *2016-06-21 08:22:35*

**hotfix issue#99**


[433498930515595](https://github.com/bsorrentino/maven-confluence-plugin/commit/433498930515595) bsorrentino *2016-06-15 13:23:12*

**updating poms for 4.9.1 branch with snapshot versions**


[5ab6ec04776872f](https://github.com/bsorrentino/maven-confluence-plugin/commit/5ab6ec04776872f) bsorrentino *2016-06-15 13:17:46*


## v4.9
### Generic changes

**Merge branch 'release/4.9'**


[523ad9c1e2d8ae3](https://github.com/bsorrentino/maven-confluence-plugin/commit/523ad9c1e2d8ae3) bsorrentino *2016-06-14 10:30:30*

**updating poms for branch'release/4.9' with non-snapshot versions**


[b44185954a8977c](https://github.com/bsorrentino/maven-confluence-plugin/commit/b44185954a8977c) bsorrentino *2016-06-14 10:25:00*

**update doc**


[dc07ffb94c0f02a](https://github.com/bsorrentino/maven-confluence-plugin/commit/dc07ffb94c0f02a) bsorrentino *2016-06-14 10:24:01*

**update javadoc**


[a43b6f3189223cf](https://github.com/bsorrentino/maven-confluence-plugin/commit/a43b6f3189223cf) bsorrentino *2016-06-12 21:08:30*

**Merge branch 'PR#105' into develop**


[0259d720fff2abd](https://github.com/bsorrentino/maven-confluence-plugin/commit/0259d720fff2abd) bsorrentino *2016-06-12 18:55:48*

**Merge branch 'master' of https://github.com/pbaris/maven-confluence-plugin into PR#105**


[82b46b2cd788979](https://github.com/bsorrentino/maven-confluence-plugin/commit/82b46b2cd788979) bsorrentino *2016-06-12 18:53:37*

**update comment**


[8ceceac5ee101c4](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ceceac5ee101c4) bsorrentino *2016-06-12 10:21:40*

**fix issue 99**


[d857a23ea8e93ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/d857a23ea8e93ac) bsorrentino *2016-06-12 10:15:39*

**update submodule**


[2a2816286baa932](https://github.com/bsorrentino/maven-confluence-plugin/commit/2a2816286baa932) bsorrentino *2016-06-10 07:48:26*

**add childrenTitlesPrefixed property**


[4c3ec9954aa6e91](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c3ec9954aa6e91) panos *2016-06-10 07:05:13*

**add childrenTitlesPrefixed property**


[1bc0105d4d4320a](https://github.com/bsorrentino/maven-confluence-plugin/commit/1bc0105d4d4320a) panos *2016-06-10 07:00:34*

**Merge branch 'release/4.8' into develop**


[ef4a08a9d7d0d72](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef4a08a9d7d0d72) bsorrentino *2016-05-21 07:25:05*


## v4.8
### Generic changes

**Merge branch 'release/4.8'**


[c6590450ec9968e](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6590450ec9968e) bsorrentino *2016-05-21 07:24:57*

**update doc for release 4.8**


[7223d910635847b](https://github.com/bsorrentino/maven-confluence-plugin/commit/7223d910635847b) bsorrentino *2016-05-21 07:24:44*

**Merge branch 'feature/issue#102' into develop**


[508752534ad0d5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/508752534ad0d5b) bsorrentino *2016-05-19 15:10:38*

**remove banner**


[1d97e9f7745066f](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d97e9f7745066f) bsorrentino *2016-05-19 15:09:46*

**use getTitle() instead a custom name in processMojoDescriptors**


[3a3c6988cf77d7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a3c6988cf77d7b) bsorrentino *2016-05-19 14:15:52*

**add ${project.summary} support**


[edd7c0db2386aa8](https://github.com/bsorrentino/maven-confluence-plugin/commit/edd7c0db2386aa8) bsorrentino *2016-05-19 13:41:37*

**Merge branch 'feature/issue#100' into develop**


[3341d4fc3634a1a](https://github.com/bsorrentino/maven-confluence-plugin/commit/3341d4fc3634a1a) bsorrentino *2016-05-16 09:45:56*

**reuse 'project vars translation' also for 'plugin doc generation'**


[97e4456e4dd4b6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/97e4456e4dd4b6b) bsorrentino *2016-05-16 09:44:18*

**Merge branch 'hotfix/readme' into develop**


[86eba5297b441e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/86eba5297b441e2) bsorrentino *2016-05-09 14:53:21*

**Merge branch 'hotfix/readme'**


[4006d2f08d24b5b](https://github.com/bsorrentino/maven-confluence-plugin/commit/4006d2f08d24b5b) bsorrentino *2016-05-09 14:53:19*

**update doc**


[f5924116cc7ad14](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5924116cc7ad14) bsorrentino *2016-05-09 14:52:49*

**Merge branch 'master' into develop**


[70f21c8f4e20759](https://github.com/bsorrentino/maven-confluence-plugin/commit/70f21c8f4e20759) bsorrentino *2016-05-09 14:02:40*

**Updating develop poms back to pre merge state**


[23c5f62b922ab6d](https://github.com/bsorrentino/maven-confluence-plugin/commit/23c5f62b922ab6d) bsorrentino *2016-05-09 14:02:40*

**updating develop poms to master versions to avoid merge conflicts**


[948c4464b7ef4f7](https://github.com/bsorrentino/maven-confluence-plugin/commit/948c4464b7ef4f7) bsorrentino *2016-05-09 14:02:39*

**updating poms for 4.8-SNAPSHOT development**


[3c7dc239b58212f](https://github.com/bsorrentino/maven-confluence-plugin/commit/3c7dc239b58212f) bsorrentino *2016-05-09 14:00:02*

**refactor generateProjectReport method**


[eb110a1e475af85](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb110a1e475af85) bsorrentino *2016-05-09 13:52:19*


## v4.7
### Generic changes

**Merge branch 'release/4.7'**


[cc711277a64ff36](https://github.com/bsorrentino/maven-confluence-plugin/commit/cc711277a64ff36) bsorrentino *2016-05-09 14:02:37*

**updating poms for branch'release/4.7' with non-snapshot versions**


[34e35c5a9bbbfe6](https://github.com/bsorrentino/maven-confluence-plugin/commit/34e35c5a9bbbfe6) bsorrentino *2016-05-09 14:01:59*

**Merge branch 'feature/issue#98' into develop**


[a064152a3ea72c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/a064152a3ea72c3) bsorrentino *2016-05-04 16:06:31*

**add support to "homePageTitle" in markdown serialization process**


[9f6951043e57f3c](https://github.com/bsorrentino/maven-confluence-plugin/commit/9f6951043e57f3c) bsorrentino *2016-05-04 16:06:12*

**add support to "parentPageTitle" in markdown serialization process**


[647a52c17b4c2e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/647a52c17b4c2e5) bsorrentino *2016-05-04 09:26:21*

**md test refinements**


[aac6e593afb0fbe](https://github.com/bsorrentino/maven-confluence-plugin/commit/aac6e593afb0fbe) bsorrentino *2016-05-01 10:27:26*

**add enforcer plugin (no snapshot allowed)**


[387ab8319304014](https://github.com/bsorrentino/maven-confluence-plugin/commit/387ab8319304014) bsorrentino *2016-04-11 10:06:00*

**move to next dev version**


[c1411daccac907c](https://github.com/bsorrentino/maven-confluence-plugin/commit/c1411daccac907c) bsorrentino *2016-04-06 13:52:07*

**Merge branch 'release/4.6' into develop**


[358f1512cb85597](https://github.com/bsorrentino/maven-confluence-plugin/commit/358f1512cb85597) bsorrentino *2016-04-06 13:47:20*


## v4.6
### Generic changes

**Merge branch 'release/4.6'**


[e69dec655464e8f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e69dec655464e8f) bsorrentino *2016-04-06 13:47:09*

**update doc**


[2633d0dc7a3b252](https://github.com/bsorrentino/maven-confluence-plugin/commit/2633d0dc7a3b252) bsorrentino *2016-04-06 13:36:40*

**prepare for release**


[f9e3a073e5e313b](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9e3a073e5e313b) bsorrentino *2016-04-06 12:52:48*

**Merge branch 'feature/issue#93' into develop**


[17e5c2dd5f2e05c](https://github.com/bsorrentino/maven-confluence-plugin/commit/17e5c2dd5f2e05c) bsorrentino *2016-03-31 21:31:56*

**finalize team render support**


[d824661fa206dcb](https://github.com/bsorrentino/maven-confluence-plugin/commit/d824661fa206dcb) bsorrentino *2016-03-31 21:30:00*

**refine ProjectTeamRenderer**


[1c8e57d351d9769](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c8e57d351d9769) bsorrentino *2016-03-31 21:17:39*

**add ProjectTeamRenderer**


[5fd176e8804233c](https://github.com/bsorrentino/maven-confluence-plugin/commit/5fd176e8804233c) bsorrentino *2016-03-31 20:39:36*

**Merge branch 'hotfix/doc' into develop**


[16d0f10af08fffd](https://github.com/bsorrentino/maven-confluence-plugin/commit/16d0f10af08fffd) bsorrentino *2016-03-31 15:16:51*

**Merge branch 'hotfix/doc'**


[35129b2d213a96c](https://github.com/bsorrentino/maven-confluence-plugin/commit/35129b2d213a96c) bsorrentino *2016-03-31 15:16:48*

**update doc**


[3aaf77eba48bc45](https://github.com/bsorrentino/maven-confluence-plugin/commit/3aaf77eba48bc45) bsorrentino *2016-03-31 15:16:25*

**update minitemplator version**


[2369ecad921715b](https://github.com/bsorrentino/maven-confluence-plugin/commit/2369ecad921715b) bsorrentino *2016-03-29 23:31:08*

**update minitemplator dev version to test issue94**


[1855b43314b7d88](https://github.com/bsorrentino/maven-confluence-plugin/commit/1855b43314b7d88) bsorrentino *2016-03-21 10:32:57*

**prepare next release**


[4d45ebf55bcb8bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/4d45ebf55bcb8bb) bsorrentino *2016-02-11 23:01:08*

**Merge branch 'release/4.5' into develop**


[7a5dacafd4c4c89](https://github.com/bsorrentino/maven-confluence-plugin/commit/7a5dacafd4c4c89) bsorrentino *2016-02-11 22:52:22*


## v4.5
### Generic changes

**Merge branch 'release/4.5'**


[db883b34c1bd2a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/db883b34c1bd2a7) bsorrentino *2016-02-11 22:52:05*

**arranged release 4.5**


[466e318256ca822](https://github.com/bsorrentino/maven-confluence-plugin/commit/466e318256ca822) bsorrentino *2016-02-11 22:51:47*

**fix issue#91**

 * update scmRenderer

[772eecb2d39cb6e](https://github.com/bsorrentino/maven-confluence-plugin/commit/772eecb2d39cb6e) bsorrentino *2016-02-10 20:39:13*

**update readme**

 * Merge branch &#x27;hotfix/docs&#x27; into develop

[e8f43d417b43194](https://github.com/bsorrentino/maven-confluence-plugin/commit/e8f43d417b43194) bsorrentino *2016-01-03 15:53:40*

**Merge branch 'hotfix/docs'**


[85621fa7d5d2b40](https://github.com/bsorrentino/maven-confluence-plugin/commit/85621fa7d5d2b40) bsorrentino *2016-01-03 15:53:37*

**update readme**


[87052fb55686ddf](https://github.com/bsorrentino/maven-confluence-plugin/commit/87052fb55686ddf) bsorrentino *2016-01-03 15:53:32*

**update readme**

 * Merge branch &#x27;hotfix/docs&#x27; into develop

[d4e6418c313e408](https://github.com/bsorrentino/maven-confluence-plugin/commit/d4e6418c313e408) bsorrentino *2016-01-03 15:51:02*

**Merge branch 'hotfix/docs'**


[fd8a12daf50b8e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/fd8a12daf50b8e2) bsorrentino *2016-01-03 15:50:59*

**update readme**


[ac36b5bb60526a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/ac36b5bb60526a8) bsorrentino *2016-01-03 15:50:56*

**update docs**

 * Merge branch &#x27;hotfix/docs&#x27; into develop

[1c9f82e6824d982](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c9f82e6824d982) bsorrentino *2016-01-03 15:48:05*

**Merge branch 'hotfix/docs'**


[4aa7bdefa735142](https://github.com/bsorrentino/maven-confluence-plugin/commit/4aa7bdefa735142) bsorrentino *2016-01-03 15:48:01*

**update docs**


[17e8a130780646e](https://github.com/bsorrentino/maven-confluence-plugin/commit/17e8a130780646e) bsorrentino *2016-01-03 15:47:18*

**merged from release 4.4.3**


[e029d0caac884bc](https://github.com/bsorrentino/maven-confluence-plugin/commit/e029d0caac884bc) bsorrentino *2015-12-29 13:58:43*

**updating poms for 4.4.4-SNAPSHOT development**


[18200679cd172fd](https://github.com/bsorrentino/maven-confluence-plugin/commit/18200679cd172fd) bsorrentino *2015-12-29 13:43:33*


## v4.4.3
### Generic changes

**Merge branch 'release/4.4.3'**


[89a8cbd7213ab7c](https://github.com/bsorrentino/maven-confluence-plugin/commit/89a8cbd7213ab7c) bsorrentino *2015-12-29 13:55:01*

**update version**


[8497e119b267dc2](https://github.com/bsorrentino/maven-confluence-plugin/commit/8497e119b267dc2) bsorrentino *2015-12-29 13:53:13*

**Merge branch 'feature/pull90' into develop**

 * Merged pull request https://github.com/bsorrentino/maven-confluence-plugin/pull/90 from https://github.com/gmuecke

[f82f02c98955c12](https://github.com/bsorrentino/maven-confluence-plugin/commit/f82f02c98955c12) bsorrentino *2015-12-21 09:40:47*

**Merge branch 'master' of https://github.com/devcon5io/maven-confluence-plugin into feature/pull90**


[e647042aa2e198c](https://github.com/bsorrentino/maven-confluence-plugin/commit/e647042aa2e198c) bsorrentino *2015-12-21 09:37:03*

**update forge module**


[f97bdb3d18e04ef](https://github.com/bsorrentino/maven-confluence-plugin/commit/f97bdb3d18e04ef) bsorrentino *2015-12-21 09:34:48*

**fixed formattings**


[8a45519b90b0952](https://github.com/bsorrentino/maven-confluence-plugin/commit/8a45519b90b0952) Gerald Mücke *2015-12-19 19:59:33*

**improved list generation**

 * -fixed missing new line at the end of bullet  list
 * -added support for ordered lists

[d76f262e16afe6f](https://github.com/bsorrentino/maven-confluence-plugin/commit/d76f262e16afe6f) Gerald Mücke *2015-12-19 19:39:29*

**Merge branch 'hotfix/site' into develop**


[6f6ac2e1a2278a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f6ac2e1a2278a7) bsorrentino *2015-12-01 23:52:09*

**Merge branch 'hotfix/site'**


[7dadd090130fe60](https://github.com/bsorrentino/maven-confluence-plugin/commit/7dadd090130fe60) bsorrentino *2015-12-01 23:52:03*

**add confluence notation guide to site**


[f7863fa99747889](https://github.com/bsorrentino/maven-confluence-plugin/commit/f7863fa99747889) bsorrentino *2015-12-01 23:50:15*

**move to next dev version**


[092459f3a5d2757](https://github.com/bsorrentino/maven-confluence-plugin/commit/092459f3a5d2757) bsorrentino *2015-11-21 10:35:12*

**Updating develop poms back to pre merge state**


[5fda013d01dc9f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/5fda013d01dc9f5) bsorrentino *2015-11-21 09:37:10*

**Merge branch 'master' into develop**


[753ffed3a408cd8](https://github.com/bsorrentino/maven-confluence-plugin/commit/753ffed3a408cd8) bsorrentino *2015-11-21 09:37:09*

**updating develop poms to master versions to avoid merge conflicts**


[05c048b21f1b429](https://github.com/bsorrentino/maven-confluence-plugin/commit/05c048b21f1b429) bsorrentino *2015-11-21 09:37:09*

**updating poms for 4.4.3-SNAPSHOT development**


[c01334706310760](https://github.com/bsorrentino/maven-confluence-plugin/commit/c01334706310760) bsorrentino *2015-11-21 09:33:51*


## v4.4.2
### Generic changes

**Merge branch 'release/4.4.2'**


[ce18028bda8a8c5](https://github.com/bsorrentino/maven-confluence-plugin/commit/ce18028bda8a8c5) bsorrentino *2015-11-21 09:38:05*

**updating poms for branch'release/4.4.2' with non-snapshot versions**


[9872364f28d4f45](https://github.com/bsorrentino/maven-confluence-plugin/commit/9872364f28d4f45) bsorrentino *2015-11-21 09:37:07*

**update gitflow plugin configuration**


[a6440ce27ce4212](https://github.com/bsorrentino/maven-confluence-plugin/commit/a6440ce27ce4212) bsorrentino *2015-10-27 18:59:58*

**improve markdown support**


[ef0b7396ec97e29](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef0b7396ec97e29) bsorrentino *2015-10-12 21:31:21*

**Merge remote-tracking branch 'origin/develop' into develop**


[c19fc7bc8617ba1](https://github.com/bsorrentino/maven-confluence-plugin/commit/c19fc7bc8617ba1) bsorrentino *2015-10-11 13:45:07*

**fix style**


[e388ae39b0fd9fa](https://github.com/bsorrentino/maven-confluence-plugin/commit/e388ae39b0fd9fa) bsorrentino *2015-10-11 13:44:33*

**fix style**


[a282623a4c43e32](https://github.com/bsorrentino/maven-confluence-plugin/commit/a282623a4c43e32) bsorrentino *2015-10-11 13:41:17*

**Update README.md**


[0a3db4df9cad99f](https://github.com/bsorrentino/maven-confluence-plugin/commit/0a3db4df9cad99f) bsorrentino *2015-10-11 13:38:48*

**fix stylet**


[63fd215b9cf2e00](https://github.com/bsorrentino/maven-confluence-plugin/commit/63fd215b9cf2e00) bsorrentino *2015-10-11 13:34:33*

**fix stylet**


[1161e830f78ac60](https://github.com/bsorrentino/maven-confluence-plugin/commit/1161e830f78ac60) bsorrentino *2015-10-11 13:33:29*

**fix conflict**


[8fe621ddc054312](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fe621ddc054312) bsorrentino *2015-10-11 13:31:45*

**fix conflict**


[f9accd2e5051f97](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9accd2e5051f97) bsorrentino *2015-10-11 13:30:49*

**fix conflict**


[0c4f5e4e34cde4e](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c4f5e4e34cde4e) bsorrentino *2015-10-11 13:30:31*

**fix conflict**


[6a3706441e3c47d](https://github.com/bsorrentino/maven-confluence-plugin/commit/6a3706441e3c47d) bsorrentino *2015-10-11 13:27:23*

**fix conflict**


[d8e8531ec14cd59](https://github.com/bsorrentino/maven-confluence-plugin/commit/d8e8531ec14cd59) bsorrentino *2015-10-11 13:27:05*

**merge hotfix**


[19b075fa1d0ea5f](https://github.com/bsorrentino/maven-confluence-plugin/commit/19b075fa1d0ea5f) bsorrentino *2015-10-11 13:26:06*

**Merge branch 'hotfix/fix-doc'**


[4950f56b7b02229](https://github.com/bsorrentino/maven-confluence-plugin/commit/4950f56b7b02229) bsorrentino *2015-10-11 13:24:37*

**update readme**


[16742e988f58e6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/16742e988f58e6b) bsorrentino *2015-10-11 13:23:02*

**update documentation**


[1dda6eb1946bd92](https://github.com/bsorrentino/maven-confluence-plugin/commit/1dda6eb1946bd92) bsorrentino *2015-10-11 12:53:18*

**update readme**


[d1e7b8ce27a98b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1e7b8ce27a98b6) bsorrentino *2015-10-10 19:53:55*

**Merge pull request #87 from gitter-badger/gitter-badge**

 * Add a Gitter chat badge to README.md

[e57a0a88064e9e8](https://github.com/bsorrentino/maven-confluence-plugin/commit/e57a0a88064e9e8) bsorrentino *2015-10-08 10:20:00*

**Add Gitter badge**


[ac39d286ee459a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/ac39d286ee459a8) The Gitter Badger *2015-10-07 20:04:34*

**Merge branch 'release/4.4.1'**


[9927bccb13e07f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/9927bccb13e07f3) bsorrentino *2015-10-03 13:57:50*

**updating poms for branch'release/4.4.1' with non-snapshot versions**


[3583b6a6758b5cf](https://github.com/bsorrentino/maven-confluence-plugin/commit/3583b6a6758b5cf) bsorrentino *2015-10-03 13:55:39*

**update documentation (using markdown format)**


[9ec9759d16942c9](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ec9759d16942c9) bsorrentino *2015-10-03 13:41:47*

**refine confluence doc**


[aa0a87e37d11642](https://github.com/bsorrentino/maven-confluence-plugin/commit/aa0a87e37d11642) bsorrentino *2015-09-23 09:10:21*

**markdown support refinements**


[98f98ffb820dc4f](https://github.com/bsorrentino/maven-confluence-plugin/commit/98f98ffb820dc4f) bsorrentino *2015-09-22 22:17:00*

**markdown support refinements**


[0265ae2c3fe8010](https://github.com/bsorrentino/maven-confluence-plugin/commit/0265ae2c3fe8010) bsorrentino *2015-09-22 20:52:53*

**update pegdown version**

 * move documentation to markdown
 * refine support for verbatim

[db68a40a721c7d1](https://github.com/bsorrentino/maven-confluence-plugin/commit/db68a40a721c7d1) bsorrentino *2015-09-21 23:35:02*

**move documentation to markdown**


[0dfff5ac36f09cf](https://github.com/bsorrentino/maven-confluence-plugin/commit/0dfff5ac36f09cf) bsorrentino *2015-09-21 15:43:30*

**get line over error**


[8302d26c4e745a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/8302d26c4e745a5) bsorrentino *2015-09-18 16:28:53*

**add markdown 'RefLinkNode' support**


[db7eccfc97841b2](https://github.com/bsorrentino/maven-confluence-plugin/commit/db7eccfc97841b2) bsorrentino *2015-09-18 13:24:13*

**update test**


[715b740962bd8eb](https://github.com/bsorrentino/maven-confluence-plugin/commit/715b740962bd8eb) bsorrentino *2015-09-18 13:09:45*

**Merge branch 'hotfix/4.4-fix-report' into develop**


[673ffd00d5bb162](https://github.com/bsorrentino/maven-confluence-plugin/commit/673ffd00d5bb162) bsorrentino *2015-09-16 13:55:09*

**Merge branch 'hotfix/confluence-report' into develop**


[892d889db7cd21c](https://github.com/bsorrentino/maven-confluence-plugin/commit/892d889db7cd21c) bsorrentino *2015-09-16 13:29:12*

**remove eclipse files**


[3a733b38a86f2c6](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a733b38a86f2c6) bsorrentino *2015-09-05 16:38:36*

**add notation guide**


[05839b438f7c1d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/05839b438f7c1d7) bsorrentino *2015-09-05 16:36:02*

**merging 'feature/md_macro_support' into 'develop'**


[e856af37160bdf3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e856af37160bdf3) bsorrentino *2015-09-05 16:25:23*

**update md support**


[962d682a4cf0278](https://github.com/bsorrentino/maven-confluence-plugin/commit/962d682a4cf0278) bsorrentino *2015-09-05 16:24:25*

**move to next develop version**


[3a784fd6132b08e](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a784fd6132b08e) bsorrentino *2015-09-04 20:22:41*

**Merge branch 'release/4.5' into develop**


[2bdef630b12c93c](https://github.com/bsorrentino/maven-confluence-plugin/commit/2bdef630b12c93c) bsorrentino *2015-09-04 20:17:14*


## v4.4.1
### Generic changes

**Updating develop poms back to pre merge state**


[48d24fc3a710666](https://github.com/bsorrentino/maven-confluence-plugin/commit/48d24fc3a710666) bsorrentino *2015-10-03 13:57:58*

**updating develop poms to master versions to avoid merge conflicts**


[fea1bea965dc0bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/fea1bea965dc0bd) bsorrentino *2015-10-03 13:57:57*

**Merge branch 'master' into develop**


[98bc78a30c7a92e](https://github.com/bsorrentino/maven-confluence-plugin/commit/98bc78a30c7a92e) bsorrentino *2015-10-03 13:57:57*

**updating poms for 4.4.2-SNAPSHOT development**


[d64398910f2739c](https://github.com/bsorrentino/maven-confluence-plugin/commit/d64398910f2739c) bsorrentino *2015-10-03 13:47:49*


## v4.4-fix-report
### Generic changes

**Merge branch 'hotfix/4.4-fix-report'**


[583abe65c8cbf1f](https://github.com/bsorrentino/maven-confluence-plugin/commit/583abe65c8cbf1f) bsorrentino *2015-09-16 13:54:44*

**redirect confluence docs**


[541fbe04d3c7d5c](https://github.com/bsorrentino/maven-confluence-plugin/commit/541fbe04d3c7d5c) bsorrentino *2015-09-16 13:54:22*

**Merge branch 'hotfix/confluence-report'**


[979e156ba52d775](https://github.com/bsorrentino/maven-confluence-plugin/commit/979e156ba52d775) bsorrentino *2015-09-16 13:28:53*

**publish confluence site to softphone**


[985865bf8f6b18b](https://github.com/bsorrentino/maven-confluence-plugin/commit/985865bf8f6b18b) bsorrentino *2015-09-16 13:28:39*


## v4.4
### Generic changes

**Merge branch 'release/4.5'**


[65be420f93e28e9](https://github.com/bsorrentino/maven-confluence-plugin/commit/65be420f93e28e9) bsorrentino *2015-09-04 20:16:10*

**update release**


[e49769e051048dd](https://github.com/bsorrentino/maven-confluence-plugin/commit/e49769e051048dd) bsorrentino *2015-09-04 20:14:52*

**update doc**


[64f2f5ddf215045](https://github.com/bsorrentino/maven-confluence-plugin/commit/64f2f5ddf215045) bsorrentino *2015-09-04 20:02:09*

**Merge remote-tracking branch 'origin/master'**


[cca311f457460d9](https://github.com/bsorrentino/maven-confluence-plugin/commit/cca311f457460d9) bsorrentino *2015-09-03 17:13:38*

**Merge branch 'feature/qwazer-gitlog-fixes' into develop**


[07c7e50c88ae518](https://github.com/bsorrentino/maven-confluence-plugin/commit/07c7e50c88ae518) bsorrentino *2015-09-03 17:11:20*

**Merge branch 'gitlog-fixes' of https://github.com/qwazer/maven-confluence-plugin into feature/qwazer-gitlog-fixes**


[8fd9e19bd02e274](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fd9e19bd02e274) bsorrentino *2015-09-03 17:11:00*

**Merge remote-tracking branch 'origin/develop' into develop**


[15c192aac159cbf](https://github.com/bsorrentino/maven-confluence-plugin/commit/15c192aac159cbf) bsorrentino *2015-09-03 17:08:43*

**Merge branch 'feature/#84' into develop**


[d1f26d124016284](https://github.com/bsorrentino/maven-confluence-plugin/commit/d1f26d124016284) bsorrentino *2015-09-03 17:07:38*

**move ssl implementation to core module**


[1e6028c6a60c5a3](https://github.com/bsorrentino/maven-confluence-plugin/commit/1e6028c6a60c5a3) bsorrentino *2015-09-03 17:03:00*

**Merge branch 'feature/#84' into develop**


[6665eeace76ce83](https://github.com/bsorrentino/maven-confluence-plugin/commit/6665eeace76ce83) bsorrentino *2015-08-28 20:44:50*

**finalize markdown support**


[677e9e6898bb358](https://github.com/bsorrentino/maven-confluence-plugin/commit/677e9e6898bb358) bsorrentino *2015-08-28 20:44:24*

**merged from develop**


[8552dd3308bde95](https://github.com/bsorrentino/maven-confluence-plugin/commit/8552dd3308bde95) bsorrentino *2015-08-28 19:33:39*

**fix errata merge**


[ff53bbd081c0b96](https://github.com/bsorrentino/maven-confluence-plugin/commit/ff53bbd081c0b96) bsorrentino *2015-08-28 19:26:47*

**fix errata merge**


[8f81307d3988c01](https://github.com/bsorrentino/maven-confluence-plugin/commit/8f81307d3988c01) bsorrentino *2015-08-28 19:26:10*

**refine code mapping**


[ae4c62d9e36cb58](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae4c62d9e36cb58) bsorrentino *2015-08-28 18:01:14*

**finalize table processing**


[7d5ea16512dd483](https://github.com/bsorrentino/maven-confluence-plugin/commit/7d5ea16512dd483) bsorrentino *2015-08-27 14:35:39*

**start table processing**


[46d73bcef8accd6](https://github.com/bsorrentino/maven-confluence-plugin/commit/46d73bcef8accd6) bsorrentino *2015-08-26 16:09:36*

**add special panel processing**


[33176b761199ae2](https://github.com/bsorrentino/maven-confluence-plugin/commit/33176b761199ae2) bsorrentino *2015-08-25 16:02:51*

**add special panel processor from markdown to confluence**


[3fb955a08b28f66](https://github.com/bsorrentino/maven-confluence-plugin/commit/3fb955a08b28f66) bsorrentino *2015-08-21 15:56:44*

**add maven gitflow plugin support**


[fd173dce6e6e370](https://github.com/bsorrentino/maven-confluence-plugin/commit/fd173dce6e6e370) bsorrentino *2015-08-21 12:34:46*

**add maven gitflow plugin**


[0a34af0f82383f3](https://github.com/bsorrentino/maven-confluence-plugin/commit/0a34af0f82383f3) bsorrentino *2015-08-21 11:01:14*

**Merge branches 'gh-pages' and 'master' of https://github.com/bsorrentino/maven-confluence-plugin**


[be769f91c8fbf8b](https://github.com/bsorrentino/maven-confluence-plugin/commit/be769f91c8fbf8b) bsorrentino *2015-08-18 08:15:41*

**fix logic for calculating since tag name for CURRENT_MAJOR_VERSION && CURRENT_MINOR_VERSION rules.**

 * some polishing of ode and tests

[9d3ef226c2fcec7](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d3ef226c2fcec7) qwazer *2015-08-16 10:45:02*

**refine code mapping**


[35bc24df36c00ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/35bc24df36c00ab) bsorrentino *2015-08-14 22:05:57*

**start implementing a pegdown confluence serializer**


[88ddd7d91f9d1f0](https://github.com/bsorrentino/maven-confluence-plugin/commit/88ddd7d91f9d1f0) bsorrentino *2015-08-14 20:47:49*

**move markdown test on core module**


[26108500697acc8](https://github.com/bsorrentino/maven-confluence-plugin/commit/26108500697acc8) bsorrentino *2015-08-14 19:56:45*

**move markdown test on core module**


[aa1dd7f52a3efd6](https://github.com/bsorrentino/maven-confluence-plugin/commit/aa1dd7f52a3efd6) bsorrentino *2015-08-14 19:56:16*

**test refinement**


[152de7a0dde3caa](https://github.com/bsorrentino/maven-confluence-plugin/commit/152de7a0dde3caa) bsorrentino *2015-08-13 14:41:39*

**update pluginToolsVersion to 3.4 to avoid asm clash between version 3.3.1 (maven-generator-toos) and 5.0.3 (pegdown)**


[28505dc03147d00](https://github.com/bsorrentino/maven-confluence-plugin/commit/28505dc03147d00) bsorrentino *2015-08-13 14:35:25*

**add test on pegdown parser**

 * fix asm clash between version 3.3.1 (maven-generator-toos) and 5.0.3 (pegdown)

[b21f91690f4a46c](https://github.com/bsorrentino/maven-confluence-plugin/commit/b21f91690f4a46c) bsorrentino *2015-08-13 14:24:19*

**add pegdown dep**


[43e0275f9c1a28b](https://github.com/bsorrentino/maven-confluence-plugin/commit/43e0275f9c1a28b) bsorrentino *2015-08-13 09:52:31*

**Merge branch 'qwazer-gitlog-improvements' into develop**


[c3a019312eced2a](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3a019312eced2a) bsorrentino *2015-08-13 08:44:37*

**Merge branch 'gitlog-improvements' of https://github.com/qwazer/maven-confluence-plugin into qwazer-gitlog-improvements**


[ed216298c656da1](https://github.com/bsorrentino/maven-confluence-plugin/commit/ed216298c656da1) bsorrentino *2015-08-13 08:43:41*

**add notation guide**


[483dc71e28ea255](https://github.com/bsorrentino/maven-confluence-plugin/commit/483dc71e28ea255) bsorrentino *2015-08-03 09:53:18*

**update doc**


[6625e8c72114c9d](https://github.com/bsorrentino/maven-confluence-plugin/commit/6625e8c72114c9d) bsorrentino *2015-07-18 18:19:17*

**update doc**


[71c0481160ec294](https://github.com/bsorrentino/maven-confluence-plugin/commit/71c0481160ec294) bsorrentino *2015-07-18 18:18:15*

**update doc**


[b6bba4d0a5066cd](https://github.com/bsorrentino/maven-confluence-plugin/commit/b6bba4d0a5066cd) bsorrentino *2015-07-18 18:17:32*

**update doc**


[7df516d4cfffec2](https://github.com/bsorrentino/maven-confluence-plugin/commit/7df516d4cfffec2) bsorrentino *2015-07-18 18:16:51*

**update doc**


[739fea59c00f947](https://github.com/bsorrentino/maven-confluence-plugin/commit/739fea59c00f947) bsorrentino *2015-07-18 18:15:46*

**Creating site for 4.3**


[065722c99318a04](https://github.com/bsorrentino/maven-confluence-plugin/commit/065722c99318a04) bsorrentino *2015-07-18 18:13:01*

**Creating site for 4.3**


[6ab4481dd2e0670](https://github.com/bsorrentino/maven-confluence-plugin/commit/6ab4481dd2e0670) bsorrentino *2015-07-18 18:05:13*

**update maven release plugin version**


[0b3c2ef355002a1](https://github.com/bsorrentino/maven-confluence-plugin/commit/0b3c2ef355002a1) bsorrentino *2015-07-18 17:53:47*

**moved to next development release**


[1cbc454e5cc4a57](https://github.com/bsorrentino/maven-confluence-plugin/commit/1cbc454e5cc4a57) bsorrentino *2015-07-18 17:51:09*

**issue#82**

 * fix nonProxyHosts issue

[3e89e71cc621d76](https://github.com/bsorrentino/maven-confluence-plugin/commit/3e89e71cc621d76) bsorrentino *2015-07-13 16:46:12*

**issue#82 add test to parse nonProxyHosts**


[14f293014e3ee3f](https://github.com/bsorrentino/maven-confluence-plugin/commit/14f293014e3ee3f) bsorrentino *2015-07-12 21:46:34*

**Exclude current version when calculate since tag name by rule.**

 * It can be useful with some cobinations of maven-release-plugin &amp;  confluence-reporting-maven-plugin.
 * For example, maven release-plugin creates git tag with current version.
 * This tag can break CalculateRuleForSinceTagName findings

[52a4de51c4b9632](https://github.com/bsorrentino/maven-confluence-plugin/commit/52a4de51c4b9632) qwazer *2015-07-11 12:11:42*

**update to newest jgit version**


[719bf8ee8ab4fce](https://github.com/bsorrentino/maven-confluence-plugin/commit/719bf8ee8ab4fce) qwazer *2015-07-11 10:30:55*

**update namespace**


[b343695c0906030](https://github.com/bsorrentino/maven-confluence-plugin/commit/b343695c0906030) bsorrentino *2015-07-03 21:48:38*

**issue#88**

 * update docs

[3a8e494751a947c](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a8e494751a947c) bsorrentino *2015-07-03 21:00:53*

**issue#88**

 * update docs

[39e9ef1e6758c02](https://github.com/bsorrentino/maven-confluence-plugin/commit/39e9ef1e6758c02) bsorrentino *2015-07-03 10:43:16*

**issue#88**

 * add ${childTitle} variable to child page
 * need to be tested

[0895bc23b827513](https://github.com/bsorrentino/maven-confluence-plugin/commit/0895bc23b827513) bsorrentino *2015-07-02 15:10:53*

**update README**


[5901f6f372815a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/5901f6f372815a5) bsorrentino *2015-06-22 15:49:17*

**update README**


[4ec3158e47a9701](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ec3158e47a9701) bsorrentino *2015-06-22 15:45:39*

**add notation guide**


[2cd70df9c18e858](https://github.com/bsorrentino/maven-confluence-plugin/commit/2cd70df9c18e858) bsorrentino *2015-06-22 15:43:42*

**update project url**


[6f2fba2de247d69](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f2fba2de247d69) bsorrentino *2015-06-22 08:55:43*

**Update README.md**


[0987eb2843cebb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/0987eb2843cebb2) bsorrentino *2015-06-04 08:29:46*

**update doc**


[da8229ca6938258](https://github.com/bsorrentino/maven-confluence-plugin/commit/da8229ca6938258) bsorrentino *2015-05-18 16:14:17*

**update doc**


[ab2f2d5fcf6b2a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/ab2f2d5fcf6b2a8) bsorrentino *2015-05-18 16:13:12*

**update doc**


[3e53d071ba9ece2](https://github.com/bsorrentino/maven-confluence-plugin/commit/3e53d071ba9ece2) bsorrentino *2015-05-18 16:10:30*

**update url**

 * update doc

[c01bd80c28e60bf](https://github.com/bsorrentino/maven-confluence-plugin/commit/c01bd80c28e60bf) bsorrentino *2015-05-18 16:09:11*

**update release**

 * fix site url
 * fix javadoc error using java8

[882f429308ffe85](https://github.com/bsorrentino/maven-confluence-plugin/commit/882f429308ffe85) bsorrentino *2015-05-18 13:58:32*

**Creating site for 4.2**


[82fb9ac3d6406cc](https://github.com/bsorrentino/maven-confluence-plugin/commit/82fb9ac3d6406cc) bsorrentino *2015-05-18 13:55:58*

**Creating site for 4.1.1**


[2ef6e599fc4aa18](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ef6e599fc4aa18) bsorrentino *2015-01-19 22:59:22*

**notation guide**


[a9d13f5d7d8d80e](https://github.com/bsorrentino/maven-confluence-plugin/commit/a9d13f5d7d8d80e) bsorrentino *2014-07-25 15:56:00*

**Creating site for 4.1.0**


[3412cb5970ff9d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/3412cb5970ff9d5) bsorrentino *2014-07-08 18:39:57*

**Creating site for 4.0.0**


[d562e1532e7634c](https://github.com/bsorrentino/maven-confluence-plugin/commit/d562e1532e7634c) bsorrentino *2014-03-28 18:27:11*

**Creating site for 4.0.0-beta1**


[543a6037c116684](https://github.com/bsorrentino/maven-confluence-plugin/commit/543a6037c116684) bsorrentino *2014-03-22 18:10:41*

**Creating site for 4.0.0-beta1**


[f0a579e0ea9c433](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0a579e0ea9c433) bsorrentino *2014-03-07 22:26:36*

**Creating site for 4.0.0-beta1**


[dd3815cf04a32df](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd3815cf04a32df) bsorrentino *2014-03-07 22:26:14*

**Creating site for 4.0.0-beta1**


[9b490c70f935f6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/9b490c70f935f6b) bsorrentino *2014-03-07 22:22:26*

**Creating site for 4.0.0-SNAPSHOT**


[2f14178ea40d508](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f14178ea40d508) bsorrentino *2014-03-07 21:16:26*


## maven-confluence-parent-4.2
### Generic changes

**merge pull request #76**

 * update site (apt) documentation

[a1f2efd49d75dbd](https://github.com/bsorrentino/maven-confluence-plugin/commit/a1f2efd49d75dbd) bsorrentino *2015-05-18 10:36:35*

**[minor] .gitignore**


[fa54051d2742d7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa54051d2742d7b) qwazer *2015-05-17 08:43:46*

**[minor] readme update**


[ceb3cc17850447a](https://github.com/bsorrentino/maven-confluence-plugin/commit/ceb3cc17850447a) qwazer *2015-05-17 08:43:05*

**readme update**


[7f7c3cee4ab5543](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f7c3cee4ab5543) qwazer *2015-05-17 08:40:02*

**readme update**


[92023ccff005cd1](https://github.com/bsorrentino/maven-confluence-plugin/commit/92023ccff005cd1) qwazer *2015-05-17 08:38:43*

**[minor] revert version to 4.2-SNAPSHOT**


[7ad43b59f2e808a](https://github.com/bsorrentino/maven-confluence-plugin/commit/7ad43b59f2e808a) qwazer *2015-05-17 08:22:40*

**[minor] code cleanup and some comments**


[82962f4fc4d4fbd](https://github.com/bsorrentino/maven-confluence-plugin/commit/82962f4fc4d4fbd) qwazer *2015-05-17 08:20:26*

**prevent possible ArrayIndexOutOfBoundsException**


[e563bb9633d9cf4](https://github.com/bsorrentino/maven-confluence-plugin/commit/e563bb9633d9cf4) qwazer *2015-05-16 16:12:22*

**docs**


[4c8609fc9946322](https://github.com/bsorrentino/maven-confluence-plugin/commit/4c8609fc9946322) qwazer *2015-05-16 16:12:22*

**prevent SLF4J messages**


[890ac89078564e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/890ac89078564e6) qwazer *2015-05-16 16:12:21*

**some improvements**


[1f4c3c4a0bb4df6](https://github.com/bsorrentino/maven-confluence-plugin/commit/1f4c3c4a0bb4df6) qwazer *2015-05-16 16:12:21*

**delete it-tests. it will be unusable soon after https://sample004.atlassian.net/ trial will expire**


[805caf4b588d2c4](https://github.com/bsorrentino/maven-confluence-plugin/commit/805caf4b588d2c4) qwazer *2015-05-12 18:59:09*

**docs**


[c0ebd012c7058ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/c0ebd012c7058ff) qwazer *2015-05-12 18:54:12*

**some renames again**


[ba48184b63a03ae](https://github.com/bsorrentino/maven-confluence-plugin/commit/ba48184b63a03ae) qwazer *2015-05-12 18:22:35*

**some renames and docs**


[052737007ee4871](https://github.com/bsorrentino/maven-confluence-plugin/commit/052737007ee4871) qwazer *2015-05-12 18:19:07*

**preserve insertion order when extractJiraIssues**


[bc4fbe2c11c1010](https://github.com/bsorrentino/maven-confluence-plugin/commit/bc4fbe2c11c1010) qwazer *2015-05-11 12:04:03*

**improved renderer**


[8323777e07c3d4e](https://github.com/bsorrentino/maven-confluence-plugin/commit/8323777e07c3d4e) qwazer *2015-05-11 11:59:44*

**add new parameter gitLogGroupByVersions**


[c63b8319abb3ca2](https://github.com/bsorrentino/maven-confluence-plugin/commit/c63b8319abb3ca2) qwazer *2015-05-11 11:49:31*

**some clean up**


[8092dc98fed50f5](https://github.com/bsorrentino/maven-confluence-plugin/commit/8092dc98fed50f5) qwazer *2015-05-11 10:15:59*

**rewritre GitLogHelper.java as GitLogUtil.java**


[259d174fee6c487](https://github.com/bsorrentino/maven-confluence-plugin/commit/259d174fee6c487) qwazer *2015-05-11 10:14:48*

**update jgit version**


[10e5cebc11daa36](https://github.com/bsorrentino/maven-confluence-plugin/commit/10e5cebc11daa36) qwazer *2015-05-11 08:21:13*

**move out formatting logic from GitLogHelper.java**


[106499776dae418](https://github.com/bsorrentino/maven-confluence-plugin/commit/106499776dae418) qwazer *2015-05-11 08:09:45*

**[minor] javadoc**


[e95f29b0450431b](https://github.com/bsorrentino/maven-confluence-plugin/commit/e95f29b0450431b) qwazer *2015-05-11 07:44:18*

**more tests**


[c97c298924506de](https://github.com/bsorrentino/maven-confluence-plugin/commit/c97c298924506de) qwazer *2015-05-11 07:35:37*

**more cases**


[d52cdcf9d24da6b](https://github.com/bsorrentino/maven-confluence-plugin/commit/d52cdcf9d24da6b) qwazer *2015-05-08 19:17:47*

**renames**


[650362eab3b9a16](https://github.com/bsorrentino/maven-confluence-plugin/commit/650362eab3b9a16) qwazer *2015-05-08 17:23:01*

**big patch**


[0875733ca2d8728](https://github.com/bsorrentino/maven-confluence-plugin/commit/0875733ca2d8728) qwazer *2015-05-08 16:58:56*

**add more parameters**


[7b36cf8758f383f](https://github.com/bsorrentino/maven-confluence-plugin/commit/7b36cf8758f383f) qwazer *2015-05-07 18:35:32*

**rename refactoring**


[0c5f27c1b6a0e7b](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c5f27c1b6a0e7b) qwazer *2015-05-07 17:57:45*

**logging**


[c3623450906ed1c](https://github.com/bsorrentino/maven-confluence-plugin/commit/c3623450906ed1c) qwazer *2015-05-07 17:24:11*

**add param jiraProjectKeyList**


[2f9e5f73e0d5eae](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f9e5f73e0d5eae) qwazer *2015-05-04 13:34:53*

**4.2-UNOFFICIAL-WITH-GITLOG-SNAPSHOT**


[0ac5c8791340569](https://github.com/bsorrentino/maven-confluence-plugin/commit/0ac5c8791340569) qwazer *2015-05-04 13:03:14*

**rename refactoring**


[de85d2ed5393d62](https://github.com/bsorrentino/maven-confluence-plugin/commit/de85d2ed5393d62) qwazer *2015-05-04 13:02:54*

**gitLogSinceTagName & gitLogSinceTagNameVersionRule**


[11d1074a91e08c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/11d1074a91e08c2) qwazer *2015-05-04 12:53:32*

**GitLogHelper.java**


[280ff68443223ce](https://github.com/bsorrentino/maven-confluence-plugin/commit/280ff68443223ce) qwazer *2015-05-04 10:16:50*

**GitLogHelper.java**


[acb3b9f4e6b438b](https://github.com/bsorrentino/maven-confluence-plugin/commit/acb3b9f4e6b438b) qwazer *2015-05-04 09:13:51*

**add some code from gitlog maven plugin**


[dca1a6114d4071c](https://github.com/bsorrentino/maven-confluence-plugin/commit/dca1a6114d4071c) qwazer *2015-05-02 08:35:06*

**add stub for GitLogJiraIssuesRenderer.java**


[dabbcd54f8ace7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/dabbcd54f8ace7e) qwazer *2015-05-02 07:55:04*

**,gitignore**


[d4251ef67d526c8](https://github.com/bsorrentino/maven-confluence-plugin/commit/d4251ef67d526c8) qwazer *2015-05-01 09:59:55*

**sample004.atlassian.net**


[b409790644caa09](https://github.com/bsorrentino/maven-confluence-plugin/commit/b409790644caa09) qwazer *2015-05-01 09:57:34*

**update doc**


[e426eff77f75b47](https://github.com/bsorrentino/maven-confluence-plugin/commit/e426eff77f75b47) bsorrentino *2015-04-07 16:38:30*

**merged withnv4**


[b5fd1b3b3e6b747](https://github.com/bsorrentino/maven-confluence-plugin/commit/b5fd1b3b3e6b747) bsorrentino *2015-04-07 16:35:47*

**update read**


[eb2b6e3d9a54be1](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb2b6e3d9a54be1) bsorrentino *2015-04-07 16:29:17*

**remove forge module**


[5a8ba8db869f43d](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a8ba8db869f43d) bsorrentino *2015-03-19 21:23:54*

**fix pom**


[105c832911961e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/105c832911961e2) bsorrentino *2015-03-19 17:52:55*

**merged**


[e548c8657b87140](https://github.com/bsorrentino/maven-confluence-plugin/commit/e548c8657b87140) bsorrentino *2015-03-19 16:13:15*

**Create README.md**


[a09a150b5586cc5](https://github.com/bsorrentino/maven-confluence-plugin/commit/a09a150b5586cc5) bsorrentino *2015-03-19 16:07:52*

**Merge branch 'release4.1.1' into v4**


[005f6b6381352c2](https://github.com/bsorrentino/maven-confluence-plugin/commit/005f6b6381352c2) bsorrentino *2015-01-19 23:00:49*

**update github plugin**


[5f6383522f8394a](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f6383522f8394a) bsorrentino *2015-01-19 23:00:36*

**Merge branch 'release4.1.1' into v4**


[0dda106e2424427](https://github.com/bsorrentino/maven-confluence-plugin/commit/0dda106e2424427) bsorrentino *2015-01-19 22:35:50*

**[maven-release-plugin] prepare for next development iteration**


[f75e2736d9f0b77](https://github.com/bsorrentino/maven-confluence-plugin/commit/f75e2736d9f0b77) bsorrentino *2015-01-19 22:00:47*

**[maven-release-plugin] prepare release 4.1.1**


[50ff5155abf947c](https://github.com/bsorrentino/maven-confluence-plugin/commit/50ff5155abf947c) bsorrentino *2015-01-19 21:59:32*

**excluse forge from build**


[133088534be3c02](https://github.com/bsorrentino/maven-confluence-plugin/commit/133088534be3c02) bsorrentino *2015-01-19 16:06:56*

**release 4.1.1**


[19469376c0b8565](https://github.com/bsorrentino/maven-confluence-plugin/commit/19469376c0b8565) bsorrentino *2015-01-19 15:21:57*

**issue 73**


[b8b35601ae664e1](https://github.com/bsorrentino/maven-confluence-plugin/commit/b8b35601ae664e1) bsorrentino *2014-12-17 20:35:42*

**update doc**


[045b33f47105ab9](https://github.com/bsorrentino/maven-confluence-plugin/commit/045b33f47105ab9) bsorrentino *2014-10-23 11:10:08*

**update doc**


[0e1367d1a0db340](https://github.com/bsorrentino/maven-confluence-plugin/commit/0e1367d1a0db340) bsorrentino *2014-10-23 11:08:19*

**update forge to 2.10.1**


[ec72c355313fd7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec72c355313fd7e) bsorrentino *2014-10-23 10:25:45*

**Merge branch 'v4' of https://github.com/bsorrentino/maven-confluence-reporting-plugin into v4**


[94ef2eb0ca5374b](https://github.com/bsorrentino/maven-confluence-plugin/commit/94ef2eb0ca5374b) bsorrentino *2014-10-20 09:49:37*

**update docs**


[6904e74835a14ea](https://github.com/bsorrentino/maven-confluence-plugin/commit/6904e74835a14ea) bsorrentino *2014-10-20 09:48:26*

**Merge branch 'v4' of https://github.com/bsorrentino/maven-confluence-reporting-plugin into v4**


[738d32e4cc280ad](https://github.com/bsorrentino/maven-confluence-plugin/commit/738d32e4cc280ad) bsorrentino *2014-08-04 10:02:07*

**update doc**


[1d890c528514270](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d890c528514270) bsorrentino *2014-07-25 16:00:58*

**fix issue69**


[389625daf9c9e28](https://github.com/bsorrentino/maven-confluence-plugin/commit/389625daf9c9e28) bsorrentino *2014-07-16 20:31:25*

**add comments**


[8ee32eb4169b52a](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ee32eb4169b52a) bsorrentino *2014-07-08 18:44:15*

**[maven-release-plugin] prepare for next development iteration**


[80b7fc4fba3c04c](https://github.com/bsorrentino/maven-confluence-plugin/commit/80b7fc4fba3c04c) bsorrentino *2014-07-08 17:43:27*

**[maven-release-plugin] prepare release maven-confluence-parent-4.1.0-SNAPSHOT**


[beb03efce84742b](https://github.com/bsorrentino/maven-confluence-plugin/commit/beb03efce84742b) bsorrentino *2014-07-08 17:39:23*

**remove dynjs dep**


[84662b2c27722dd](https://github.com/bsorrentino/maven-confluence-plugin/commit/84662b2c27722dd) bsorrentino *2014-07-08 17:38:08*

**update release**


[6b34ebe3ecbe5dc](https://github.com/bsorrentino/maven-confluence-plugin/commit/6b34ebe3ecbe5dc) bsorrentino *2014-07-08 17:34:23*

**Merge branch 'rmannibucau-v4' into v4**


[f00981acb99701d](https://github.com/bsorrentino/maven-confluence-plugin/commit/f00981acb99701d) bsorrentino *2014-07-08 00:30:35*

**merge pull request**


[ed2b1ea351166ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/ed2b1ea351166ac) bsorrentino *2014-07-08 00:29:04*

**merge pull request**


[2ca7f8093b7937e](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ca7f8093b7937e) bsorrentino *2014-07-08 00:28:37*

**allowing to handle ssl urls**


[ea5e1ab9aa85090](https://github.com/bsorrentino/maven-confluence-plugin/commit/ea5e1ab9aa85090) Romain Manni-Bucau *2014-07-07 18:42:21*

**Update README.md**


[5bddb909aefff80](https://github.com/bsorrentino/maven-confluence-plugin/commit/5bddb909aefff80) bsorrentino *2014-05-22 23:15:50*

**Update README.md**


[950deb83ca2392e](https://github.com/bsorrentino/maven-confluence-plugin/commit/950deb83ca2392e) bsorrentino *2014-05-22 23:15:03*

**updte doc**


[15cc38669c7454e](https://github.com/bsorrentino/maven-confluence-plugin/commit/15cc38669c7454e) bsorrentino *2014-05-20 17:06:27*

**update doc**


[27bbca9f22859fc](https://github.com/bsorrentino/maven-confluence-plugin/commit/27bbca9f22859fc) bsorrentino *2014-05-20 17:02:44*

**update doc**


[2b332b1600ecdf0](https://github.com/bsorrentino/maven-confluence-plugin/commit/2b332b1600ecdf0) bsorrentino *2014-05-20 16:55:24*

**update docs**


[6e5830a0497d4a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/6e5830a0497d4a6) bsorrentino *2014-05-20 11:21:29*

**update docs**


[0d6d8856a7e5726](https://github.com/bsorrentino/maven-confluence-plugin/commit/0d6d8856a7e5726) bsorrentino *2014-05-20 11:19:33*

**update doc**


[1dba562249d99e7](https://github.com/bsorrentino/maven-confluence-plugin/commit/1dba562249d99e7) bsorrentino *2014-05-20 11:10:20*

**Merge branch 'issue#67' into v4**

 * Conflicts:
 * maven-confluence-core/pom.xml
 * maven-confluence-reporting-plugin/pom.xml
 * maven-confluence-test/pom.xml
 * pom.xml

[a23e2e35def88be](https://github.com/bsorrentino/maven-confluence-plugin/commit/a23e2e35def88be) bsorrentino *2014-05-18 17:33:31*

**prepare for release**


[67a6c742eb91fa0](https://github.com/bsorrentino/maven-confluence-plugin/commit/67a6c742eb91fa0) bsorrentino *2014-05-18 17:17:44*

**update version**


[a2e7c9fc2dbab67](https://github.com/bsorrentino/maven-confluence-plugin/commit/a2e7c9fc2dbab67) bsorrentino *2014-05-18 16:49:34*

**finalize implementation**


[008731af5ac5d90](https://github.com/bsorrentino/maven-confluence-plugin/commit/008731af5ac5d90) bsorrentino *2014-05-18 16:46:12*

**Merge remote-tracking branch 'origin/issue#67' into issue#67**


[f5560ad27ed5f9f](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5560ad27ed5f9f) bsorrentino *2014-05-12 11:18:18*

**Implement download-page goal**


[1702f3685feb1e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/1702f3685feb1e5) bsorrentino *2014-05-12 11:16:59*

**update scm coordinate**


[5b69ae606e89fe3](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b69ae606e89fe3) bsorrentino *2014-05-10 14:19:30*

**finalize 'setup' implementation**


[426c15574c5de36](https://github.com/bsorrentino/maven-confluence-plugin/commit/426c15574c5de36) bsorrentino *2014-05-09 10:18:51*

**start refactoring from forge 1.3**


[8750032eb41d2ca](https://github.com/bsorrentino/maven-confluence-plugin/commit/8750032eb41d2ca) bsorrentino *2014-05-07 12:43:40*

**add  setup command**


[4b881c10aa20af7](https://github.com/bsorrentino/maven-confluence-plugin/commit/4b881c10aa20af7) bsorrentino *2014-04-22 22:32:50*

**pom refactoring**


[02dbf5556d73c3d](https://github.com/bsorrentino/maven-confluence-plugin/commit/02dbf5556d73c3d) bsorrentino *2014-04-18 21:38:37*

**add eclipse lifecycle-mapping**


[a34df4e0e3e8a13](https://github.com/bsorrentino/maven-confluence-plugin/commit/a34df4e0e3e8a13) bsorrentino *2014-04-18 21:18:35*

**update settings for eclipse**


[dd79f59152005b8](https://github.com/bsorrentino/maven-confluence-plugin/commit/dd79f59152005b8) bsorrentino *2014-04-12 14:12:33*

**initial import**


[0e54ed1e8401549](https://github.com/bsorrentino/maven-confluence-plugin/commit/0e54ed1e8401549) bsorrentino *2014-04-12 14:07:55*

**[maven-release-plugin] prepare for next development iteration**


[3380b1eb7f6096e](https://github.com/bsorrentino/maven-confluence-plugin/commit/3380b1eb7f6096e) bsorrentino *2014-03-28 16:37:59*

**[maven-release-plugin] prepare release maven-confluence-parent-4.0.0-SNAPSHOT**


[6033f9172a72167](https://github.com/bsorrentino/maven-confluence-plugin/commit/6033f9172a72167) bsorrentino *2014-03-28 16:36:11*

**issue 66**

 * use getFullGoalName instead of getGoalName
 * use bootstrap skin

[2e0c6f963ae9abf](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e0c6f963ae9abf) bsorrentino *2014-03-22 18:21:10*

**issue 66**

 * plugin goals summary refinements
 * update doc

[17f09e97ff13968](https://github.com/bsorrentino/maven-confluence-plugin/commit/17f09e97ff13968) bsorrentino *2014-03-22 17:59:54*

**issue 66**

 * create goal in separate pages

[c147f9e4e84e100](https://github.com/bsorrentino/maven-confluence-plugin/commit/c147f9e4e84e100) bsorrentino *2014-03-22 17:38:06*

**add classes to enable functional programming**


[e6f9bef685516b3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e6f9bef685516b3) bsorrentino *2014-03-21 16:30:41*

**each goal in separate page**


[21d7d5f1ed5e2b3](https://github.com/bsorrentino/maven-confluence-plugin/commit/21d7d5f1ed5e2b3) bsorrentino *2014-03-21 16:28:52*

**update ignore**


[f44dfc4bef5eef4](https://github.com/bsorrentino/maven-confluence-plugin/commit/f44dfc4bef5eef4) bsorrentino *2014-03-07 23:13:10*

**update ignore**


[7133d5ac244be70](https://github.com/bsorrentino/maven-confluence-plugin/commit/7133d5ac244be70) bsorrentino *2014-03-07 23:11:43*

**update ignore**


[27bea8a18dc388b](https://github.com/bsorrentino/maven-confluence-plugin/commit/27bea8a18dc388b) bsorrentino *2014-03-07 23:10:43*

**update docs**


[74efb45a306cdf9](https://github.com/bsorrentino/maven-confluence-plugin/commit/74efb45a306cdf9) bsorrentino *2014-03-07 23:08:34*

**update apt doc**


[987da8ac68e430d](https://github.com/bsorrentino/maven-confluence-plugin/commit/987da8ac68e430d) bsorrentino *2014-03-07 22:28:02*

**[maven-release-plugin] prepare for next development iteration**


[7003e93199b9806](https://github.com/bsorrentino/maven-confluence-plugin/commit/7003e93199b9806) bsorrentino *2014-03-07 21:44:47*

**[maven-release-plugin] prepare release maven-confluence-parent-4.0.0-SNAPSHOT**


[1c3f0c08c546e5e](https://github.com/bsorrentino/maven-confluence-plugin/commit/1c3f0c08c546e5e) bsorrentino *2014-03-07 21:44:18*

**update minitemplator-repackaged version**


[af5cd0264ff0476](https://github.com/bsorrentino/maven-confluence-plugin/commit/af5cd0264ff0476) bsorrentino *2014-03-07 21:37:05*

**test refinements**


[7684279f9943cfb](https://github.com/bsorrentino/maven-confluence-plugin/commit/7684279f9943cfb) bsorrentino *2014-03-07 21:31:48*

**add plugin for deploying to github**


[aaba005275864bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/aaba005275864bb) bsorrentino *2014-03-07 21:19:52*

**update site infos**


[fa5f578a98aa6b2](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa5f578a98aa6b2) bsorrentino *2014-03-07 21:05:54*

**remove code concerning maven site**


[a6e2cd276398ebc](https://github.com/bsorrentino/maven-confluence-plugin/commit/a6e2cd276398ebc) bsorrentino *2014-03-07 20:41:45*

**update scm infos**


[3728402780a365e](https://github.com/bsorrentino/maven-confluence-plugin/commit/3728402780a365e) bsorrentino *2014-03-07 20:30:50*

**isolate reporting plugin stuff**


[170708c7882412d](https://github.com/bsorrentino/maven-confluence-plugin/commit/170708c7882412d) bsorrentino *2014-03-07 19:11:39*

**Create README.md**


[5f4a4273c0de369](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f4a4273c0de369) bsorrentino *2014-03-02 20:46:52*

**update dependencies**


[e96895d9bd6c29e](https://github.com/bsorrentino/maven-confluence-plugin/commit/e96895d9bd6c29e) bsorrentino *2014-01-27 11:06:01*

**issue 58**


[5110ec41ad7bb1e](https://github.com/bsorrentino/maven-confluence-plugin/commit/5110ec41ad7bb1e) bsorrentino *2014-01-26 18:46:10*

**[maven-release-plugin] prepare for next development iteration**


[630dbf4ed25aea4](https://github.com/bsorrentino/maven-confluence-plugin/commit/630dbf4ed25aea4) bsorrentino *2014-01-26 17:47:29*


## v3.4.4-rc1
### Generic changes

**[maven-release-plugin] prepare release v3.4.4-rc1**


[dda6a23657d73a9](https://github.com/bsorrentino/maven-confluence-plugin/commit/dda6a23657d73a9) bsorrentino *2014-01-26 17:47:16*

**issue 63**


[29a1e1d236e08e5](https://github.com/bsorrentino/maven-confluence-plugin/commit/29a1e1d236e08e5) bsorrentino *2014-01-14 15:41:25*

**issue63**


[61a29aa12017365](https://github.com/bsorrentino/maven-confluence-plugin/commit/61a29aa12017365) bsorrentinoissue63 *2014-01-13 22:57:38*

**issue63**


[0c7f98d4770ab82](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c7f98d4770ab82) bsorrentino *2014-01-10 22:20:01*

**add Nexus Staging Maven Plugin**


[8ff58fc7fa49a6d](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ff58fc7fa49a6d) bsorrentino *2013-12-17 17:48:03*

**[maven-release-plugin] prepare for next development iteration**


[125dc031f5bcfd6](https://github.com/bsorrentino/maven-confluence-plugin/commit/125dc031f5bcfd6) bsorrentino *2013-12-17 17:05:38*


## maven-confluence-parent-3.4.3-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.3-SNAPSHOT**


[56cfc8f95d051c6](https://github.com/bsorrentino/maven-confluence-plugin/commit/56cfc8f95d051c6) bsorrentino *2013-12-17 17:05:26*

**issue62 resolved**


[1d423ba2ac445b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d423ba2ac445b6) bsorrentino *2013-12-17 16:57:50*

**finalize rc1**


[91aa72af36a14a3](https://github.com/bsorrentino/maven-confluence-plugin/commit/91aa72af36a14a3) bsorrentino *2013-12-17 16:24:32*

**ignore test to fix java.lang.NoClassDefFoundError: org/bsc/maven/reporting/model/Site error**


[c7387b531e7d1e7](https://github.com/bsorrentino/maven-confluence-plugin/commit/c7387b531e7d1e7) bsorrentino *2013-12-17 16:03:45*

**export features api implementation**


[0251e26d3684ed3](https://github.com/bsorrentino/maven-confluence-plugin/commit/0251e26d3684ed3) bsorrentino *2013-12-17 14:25:01*

**test for export pdf file refinements**


[bdfd5d345e2ac00](https://github.com/bsorrentino/maven-confluence-plugin/commit/bdfd5d345e2ac00) bsorrentino *2013-12-17 12:27:51*

**finalize test for export pdf file**


[953e610f169352b](https://github.com/bsorrentino/maven-confluence-plugin/commit/953e610f169352b) bsorrentino *2013-12-15 23:52:11*

**[maven-release-plugin] prepare for next development iteration**


[0c0f85949dc294f](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c0f85949dc294f) bsorrentino *2013-09-11 18:54:53*


## maven-confluence-parent-3.4.2-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.2-SNAPSHOT**


[73fa3ab087beeb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/73fa3ab087beeb2) bsorrentino *2013-09-11 18:54:43*

**Issue 59**


[9e6029a95ed7595](https://github.com/bsorrentino/maven-confluence-plugin/commit/9e6029a95ed7595) bsorrentino *2013-09-08 09:40:43*

**update deploy coordinate**


[65f318d96bcd2d2](https://github.com/bsorrentino/maven-confluence-plugin/commit/65f318d96bcd2d2) softphone *2013-09-05 19:24:20*

**[maven-release-plugin] prepare for next development iteration**


[962f954afdb840a](https://github.com/bsorrentino/maven-confluence-plugin/commit/962f954afdb840a) bsorrentino *2013-09-05 18:22:59*


## maven-confluence-parent-3.4.1
### Generic changes

**Merge branch 'master' of https://code.google.com/p/maven-confluence-plugin**


[f9ddffeae2e0797](https://github.com/bsorrentino/maven-confluence-plugin/commit/f9ddffeae2e0797) bsorrentino *2013-09-05 18:22:07*

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.1-SNAPSHOT**


[d184a8b67acb66c](https://github.com/bsorrentino/maven-confluence-plugin/commit/d184a8b67acb66c) bsorrentino *2013-09-05 18:21:23*

**issue 57**


[87ed7f2eca9db99](https://github.com/bsorrentino/maven-confluence-plugin/commit/87ed7f2eca9db99) bsorrentino *2013-09-05 18:17:33*

**merged from master**


[4d30130c6e8001e](https://github.com/bsorrentino/maven-confluence-plugin/commit/4d30130c6e8001e) bsorrentino *2013-08-17 08:49:01*

**update site schema**


[b7be8c1f942c7cb](https://github.com/bsorrentino/maven-confluence-plugin/commit/b7be8c1f942c7cb) bsorrentino *2013-08-17 08:45:06*

**[maven-release-plugin] prepare for next development iteration**


[eeb4155fe6552e8](https://github.com/bsorrentino/maven-confluence-plugin/commit/eeb4155fe6552e8) bsorrentino *2013-08-14 14:56:49*

**add schemas**


[50f2fad7529f970](https://github.com/bsorrentino/maven-confluence-plugin/commit/50f2fad7529f970) bsorrentino *2013-08-06 18:27:55*

**issue 56**


[513dd677c59b6b4](https://github.com/bsorrentino/maven-confluence-plugin/commit/513dd677c59b6b4) bsorrentino *2013-08-06 18:20:21*

**issue 56**


[4b66c83e44f2b73](https://github.com/bsorrentino/maven-confluence-plugin/commit/4b66c83e44f2b73) bsorrentino *2013-08-06 18:19:38*

**Merge branch '3.4.0'**


[e5d2ee33f19bf11](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5d2ee33f19bf11) bsorrentino *2013-07-27 17:50:35*

**[maven-release-plugin] prepare for next development iteration**


[dbac9ee0a95fffa](https://github.com/bsorrentino/maven-confluence-plugin/commit/dbac9ee0a95fffa) bsorrentino *2013-07-27 17:14:52*

**merge after pull**


[f36e4179412ed91](https://github.com/bsorrentino/maven-confluence-plugin/commit/f36e4179412ed91) softphone *2013-07-27 16:15:47*

**merge after pull**


[4753395cbaed535](https://github.com/bsorrentino/maven-confluence-plugin/commit/4753395cbaed535) softphone *2013-07-27 16:15:28*

**merge after pull**


[4ce662a68fcd4e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ce662a68fcd4e6) bsorrentino *2013-07-27 16:12:46*

**rearrange project layout**


[cd965baca905e75](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd965baca905e75) softphone *2013-06-14 21:23:51*

**update scm**


[daa59c73d7bf5a0](https://github.com/bsorrentino/maven-confluence-plugin/commit/daa59c73d7bf5a0) bsorrentino *2012-10-12 16:58:19*

**update docs**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@642 6a545194-3771-11de-8a2e-13aa1706fec1

[d09a54976a6fe41](https://github.com/bsorrentino/maven-confluence-plugin/commit/d09a54976a6fe41) bartolomeo.sorrentino *2012-10-08 18:38:50*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@637 6a545194-3771-11de-8a2e-13aa1706fec1

[2bd88421167c1c7](https://github.com/bsorrentino/maven-confluence-plugin/commit/2bd88421167c1c7) bartolomeo.sorrentino *2012-10-08 16:25:58*

**[maven-release-plugin] prepare release maven-confluence-parent-3.2.4-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@635 6a545194-3771-11de-8a2e-13aa1706fec1

[00a71da061ed2b8](https://github.com/bsorrentino/maven-confluence-plugin/commit/00a71da061ed2b8) bartolomeo.sorrentino *2012-10-08 16:25:29*

**bug fix on message format**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@634 6a545194-3771-11de-8a2e-13aa1706fec1

[5cf75d755350ada](https://github.com/bsorrentino/maven-confluence-plugin/commit/5cf75d755350ada) bartolomeo.sorrentino@gmail.com *2012-10-08 16:22:31*

**update documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@633 6a545194-3771-11de-8a2e-13aa1706fec1

[4599cfe930f2ed2](https://github.com/bsorrentino/maven-confluence-plugin/commit/4599cfe930f2ed2) bartolomeo.sorrentino@gmail.com *2012-10-08 15:46:40*

**Issue 45**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@631 6a545194-3771-11de-8a2e-13aa1706fec1

[99b233be29cf887](https://github.com/bsorrentino/maven-confluence-plugin/commit/99b233be29cf887) bartolomeo.sorrentino *2012-09-24 16:30:59*

**Issue 46**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@630 6a545194-3771-11de-8a2e-13aa1706fec1

[f37f8139b7b96cb](https://github.com/bsorrentino/maven-confluence-plugin/commit/f37f8139b7b96cb) bartolomeo.sorrentino *2012-09-24 15:45:48*

**Issue 46**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@629 6a545194-3771-11de-8a2e-13aa1706fec1

[23158d939259030](https://github.com/bsorrentino/maven-confluence-plugin/commit/23158d939259030) bartolomeo.sorrentino *2012-09-24 15:45:33*

**enable maven3 reporting**

 * documentation improvements
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@625 6a545194-3771-11de-8a2e-13aa1706fec1

[5b20750e94ea7cf](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b20750e94ea7cf) bartolomeo.sorrentino *2012-08-22 14:55:11*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@622 6a545194-3771-11de-8a2e-13aa1706fec1

[260958cdcb58c07](https://github.com/bsorrentino/maven-confluence-plugin/commit/260958cdcb58c07) bartolomeo.sorrentino *2012-08-22 10:39:34*

**[maven-release-plugin] prepare release maven-confluence-parent-3.2.3-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@620 6a545194-3771-11de-8a2e-13aa1706fec1

[8ff9de27121bce6](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ff9de27121bce6) bartolomeo.sorrentino *2012-08-22 10:39:12*

**Issue 44**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@619 6a545194-3771-11de-8a2e-13aa1706fec1

[fe516c30f7d882d](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe516c30f7d882d) bartolomeo.sorrentino@gmail.com *2012-08-22 10:33:50*

**Issue 44**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@618 6a545194-3771-11de-8a2e-13aa1706fec1

[5f6338e3e72c256](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f6338e3e72c256) bartolomeo.sorrentino *2012-08-17 21:55:19*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@613 6a545194-3771-11de-8a2e-13aa1706fec1

[2ae2136837b47a3](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ae2136837b47a3) bartolomeo.sorrentino *2012-07-30 18:18:23*

**[maven-release-plugin] prepare release maven-confluence-parent-3.2.2-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@611 6a545194-3771-11de-8a2e-13aa1706fec1

[af31b8559cf457d](https://github.com/bsorrentino/maven-confluence-plugin/commit/af31b8559cf457d) bartolomeo.sorrentino *2012-07-30 18:18:02*

**Issue 39**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@610 6a545194-3771-11de-8a2e-13aa1706fec1

[e981a3b7a4fdb6a](https://github.com/bsorrentino/maven-confluence-plugin/commit/e981a3b7a4fdb6a) bartolomeo.sorrentino *2012-07-23 23:12:56*

**update docs**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@609 6a545194-3771-11de-8a2e-13aa1706fec1

[70b8f836e69a332](https://github.com/bsorrentino/maven-confluence-plugin/commit/70b8f836e69a332) bartolomeo.sorrentino *2012-07-18 23:19:37*

**update wiki doc**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@605 6a545194-3771-11de-8a2e-13aa1706fec1

[71dbbf435235e74](https://github.com/bsorrentino/maven-confluence-plugin/commit/71dbbf435235e74) bartolomeo.sorrentino *2012-07-18 22:53:13*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@604 6a545194-3771-11de-8a2e-13aa1706fec1

[676bfcea0718255](https://github.com/bsorrentino/maven-confluence-plugin/commit/676bfcea0718255) bartolomeo.sorrentino *2012-07-18 22:35:01*

**[maven-release-plugin] prepare release maven-confluence-parent-3.2.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@602 6a545194-3771-11de-8a2e-13aa1706fec1

[b6acdf255bcef7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/b6acdf255bcef7e) bartolomeo.sorrentino *2012-07-18 22:34:34*

**prepare for release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@601 6a545194-3771-11de-8a2e-13aa1706fec1

[890c58b1dfa11f0](https://github.com/bsorrentino/maven-confluence-plugin/commit/890c58b1dfa11f0) bartolomeo.sorrentino *2012-07-18 22:31:35*

**update ignore**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@600 6a545194-3771-11de-8a2e-13aa1706fec1

[f774f1654025b2d](https://github.com/bsorrentino/maven-confluence-plugin/commit/f774f1654025b2d) bartolomeo.sorrentino *2012-07-18 22:31:05*

**update ignore**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@599 6a545194-3771-11de-8a2e-13aa1706fec1

[e10ef45e5937452](https://github.com/bsorrentino/maven-confluence-plugin/commit/e10ef45e5937452) bartolomeo.sorrentino *2012-07-18 22:30:53*

**Issue 40**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@598 6a545194-3771-11de-8a2e-13aa1706fec1

[8fa5a419f30de73](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fa5a419f30de73) bartolomeo.sorrentino *2012-07-16 21:38:20*

**Issue 40**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@597 6a545194-3771-11de-8a2e-13aa1706fec1

[004af90f23a383c](https://github.com/bsorrentino/maven-confluence-plugin/commit/004af90f23a383c) bartolomeo.sorrentino *2012-07-16 21:37:02*

**Issue 40**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@596 6a545194-3771-11de-8a2e-13aa1706fec1

[1a574bba55e9e05](https://github.com/bsorrentino/maven-confluence-plugin/commit/1a574bba55e9e05) bartolomeo.sorrentino *2012-07-16 20:53:20*

**Issue 40**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@595 6a545194-3771-11de-8a2e-13aa1706fec1

[4294499b6f4dbae](https://github.com/bsorrentino/maven-confluence-plugin/commit/4294499b6f4dbae) bartolomeo.sorrentino *2012-07-16 20:53:00*

**Issue 40**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@594 6a545194-3771-11de-8a2e-13aa1706fec1

[bc77018672a9cd0](https://github.com/bsorrentino/maven-confluence-plugin/commit/bc77018672a9cd0) bartolomeo.sorrentino *2012-07-16 20:52:28*

**update ignores**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@593 6a545194-3771-11de-8a2e-13aa1706fec1

[98ba86d3fdc823c](https://github.com/bsorrentino/maven-confluence-plugin/commit/98ba86d3fdc823c) bartolomeo.sorrentino *2012-07-10 10:02:41*

**Issue 37**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@592 6a545194-3771-11de-8a2e-13aa1706fec1

[ec80b32a767c0a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec80b32a767c0a6) bartolomeo.sorrentino@gmail.com *2012-07-10 10:01:20*

**update mojo documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@591 6a545194-3771-11de-8a2e-13aa1706fec1

[a57d0a29851bc87](https://github.com/bsorrentino/maven-confluence-plugin/commit/a57d0a29851bc87) bartolomeo.sorrentino@gmail.com *2012-06-21 09:58:08*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@586 6a545194-3771-11de-8a2e-13aa1706fec1

[5b06ecbaecda77c](https://github.com/bsorrentino/maven-confluence-plugin/commit/5b06ecbaecda77c) bartolomeo.sorrentino *2012-05-25 08:48:47*

**[maven-release-plugin] prepare release maven-confluence-parent-3.2.0-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@584 6a545194-3771-11de-8a2e-13aa1706fec1

[8691b46b8274be3](https://github.com/bsorrentino/maven-confluence-plugin/commit/8691b46b8274be3) bartolomeo.sorrentino *2012-05-25 08:48:27*

**Issue 36**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@583 6a545194-3771-11de-8a2e-13aa1706fec1

[57a3349f9beb7d6](https://github.com/bsorrentino/maven-confluence-plugin/commit/57a3349f9beb7d6) bartolomeo.sorrentino@gmail.com *2012-05-24 20:05:59*

**Issue 36**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@582 6a545194-3771-11de-8a2e-13aa1706fec1

[677bcee1108ceb6](https://github.com/bsorrentino/maven-confluence-plugin/commit/677bcee1108ceb6) bartolomeo.sorrentino@gmail.com *2012-05-24 20:05:47*

**Issue 36**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@581 6a545194-3771-11de-8a2e-13aa1706fec1

[80d81d0545e053d](https://github.com/bsorrentino/maven-confluence-plugin/commit/80d81d0545e053d) bartolomeo.sorrentino@gmail.com *2012-05-24 20:05:30*

**Issue 36**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@580 6a545194-3771-11de-8a2e-13aa1706fec1

[b26beca37275e3e](https://github.com/bsorrentino/maven-confluence-plugin/commit/b26beca37275e3e) bartolomeo.sorrentino@gmail.com *2012-05-24 20:05:05*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@574 6a545194-3771-11de-8a2e-13aa1706fec1

[a1a03ac2b697de1](https://github.com/bsorrentino/maven-confluence-plugin/commit/a1a03ac2b697de1) bartolomeo.sorrentino *2012-05-03 11:06:09*

**[maven-release-plugin] prepare release maven-confluence-parent-3.1.5-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@572 6a545194-3771-11de-8a2e-13aa1706fec1

[f5f6cbf136ee9f2](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5f6cbf136ee9f2) bartolomeo.sorrentino *2012-05-03 11:05:54*

**Issue 35**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@571 6a545194-3771-11de-8a2e-13aa1706fec1

[e105e0499d43fb8](https://github.com/bsorrentino/maven-confluence-plugin/commit/e105e0499d43fb8) bartolomeo.sorrentino@gmail.com *2012-05-03 09:57:29*

**update docs plugin**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@570 6a545194-3771-11de-8a2e-13aa1706fec1

[4ccfd3f5b437438](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ccfd3f5b437438) bartolomeo.sorrentino@gmail.com *2012-04-30 11:03:54*

**update docs**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@569 6a545194-3771-11de-8a2e-13aa1706fec1

[5f24e99b326b534](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f24e99b326b534) bartolomeo.sorrentino@gmail.com *2012-04-30 11:03:04*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@564 6a545194-3771-11de-8a2e-13aa1706fec1

[4221c4f5e9fd18d](https://github.com/bsorrentino/maven-confluence-plugin/commit/4221c4f5e9fd18d) bartolomeo.sorrentino *2012-03-16 22:45:20*

**[maven-release-plugin] prepare release maven-confluence-parent-3.1.4-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@562 6a545194-3771-11de-8a2e-13aa1706fec1

[7e968970beac2d6](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e968970beac2d6) bartolomeo.sorrentino *2012-03-16 22:45:06*

**Issue 27**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@561 6a545194-3771-11de-8a2e-13aa1706fec1

[575c98643bb96bd](https://github.com/bsorrentino/maven-confluence-plugin/commit/575c98643bb96bd) bartolomeo.sorrentino *2012-03-16 22:39:14*

**Issue 27**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@560 6a545194-3771-11de-8a2e-13aa1706fec1

[7e8bfd61e543768](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e8bfd61e543768) bartolomeo.sorrentino *2012-03-16 22:38:51*

**update plugin version**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@559 6a545194-3771-11de-8a2e-13aa1706fec1

[811d16a1ffa4835](https://github.com/bsorrentino/maven-confluence-plugin/commit/811d16a1ffa4835) bartolomeo.sorrentino *2012-01-10 07:38:23*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@557 6a545194-3771-11de-8a2e-13aa1706fec1

[b04d348de1856a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/b04d348de1856a6) bartolomeo.sorrentino *2011-12-28 17:37:10*

**[maven-release-plugin] prepare release maven-confluence-parent-3.1.3-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@555 6a545194-3771-11de-8a2e-13aa1706fec1

[5263a9c78e82f90](https://github.com/bsorrentino/maven-confluence-plugin/commit/5263a9c78e82f90) bartolomeo.sorrentino *2011-12-28 17:36:56*

**update documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@553 6a545194-3771-11de-8a2e-13aa1706fec1

[48982ffb94d8ce4](https://github.com/bsorrentino/maven-confluence-plugin/commit/48982ffb94d8ce4) bartolomeo.sorrentino *2011-12-27 11:15:40*

**Issue 32 update docs**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@551 6a545194-3771-11de-8a2e-13aa1706fec1

[6dd821b0d50a756](https://github.com/bsorrentino/maven-confluence-plugin/commit/6dd821b0d50a756) bartolomeo.sorrentino *2011-12-27 11:08:37*

**Issue 32**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@550 6a545194-3771-11de-8a2e-13aa1706fec1

[3a5b5517f5dbc57](https://github.com/bsorrentino/maven-confluence-plugin/commit/3a5b5517f5dbc57) bartolomeo.sorrentino *2011-12-27 10:51:56*

**fix problems to generate plugin confluence documentation to codehaus site**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@545 6a545194-3771-11de-8a2e-13aa1706fec1

[cd7d423f29894c7](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd7d423f29894c7) bartolomeo.sorrentino *2011-12-17 19:38:01*

**Issue 30**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@544 6a545194-3771-11de-8a2e-13aa1706fec1

[686ee4834f0269f](https://github.com/bsorrentino/maven-confluence-plugin/commit/686ee4834f0269f) bartolomeo.sorrentino *2011-12-17 16:09:55*

**update documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@543 6a545194-3771-11de-8a2e-13aa1706fec1

[2e63719977565cb](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e63719977565cb) bartolomeo.sorrentino *2011-12-17 16:07:52*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@537 6a545194-3771-11de-8a2e-13aa1706fec1

[188a95f48d18c1a](https://github.com/bsorrentino/maven-confluence-plugin/commit/188a95f48d18c1a) bartolomeo.sorrentino *2011-12-09 16:07:38*

**[maven-release-plugin] prepare release maven-confluence-parent-3.1.2-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@535 6a545194-3771-11de-8a2e-13aa1706fec1

[e38c647ca3cf5e4](https://github.com/bsorrentino/maven-confluence-plugin/commit/e38c647ca3cf5e4) bartolomeo.sorrentino *2011-12-09 16:07:24*

**Issue 29**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@534 6a545194-3771-11de-8a2e-13aa1706fec1

[a60d84b5333b075](https://github.com/bsorrentino/maven-confluence-plugin/commit/a60d84b5333b075) bartolomeo.sorrentino *2011-12-09 10:26:09*

**Issue 29**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@533 6a545194-3771-11de-8a2e-13aa1706fec1

[2ea72d81d64e610](https://github.com/bsorrentino/maven-confluence-plugin/commit/2ea72d81d64e610) bartolomeo.sorrentino *2011-12-09 10:25:13*

**update documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@531 6a545194-3771-11de-8a2e-13aa1706fec1

[7a65bda957d3bb2](https://github.com/bsorrentino/maven-confluence-plugin/commit/7a65bda957d3bb2) bartolomeo.sorrentino *2011-11-22 10:19:40*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@530 6a545194-3771-11de-8a2e-13aa1706fec1

[cded2c7964279d2](https://github.com/bsorrentino/maven-confluence-plugin/commit/cded2c7964279d2) bartolomeo.sorrentino *2011-11-21 16:28:00*

**[maven-release-plugin] prepare release maven-confluence-parent-3.1.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@528 6a545194-3771-11de-8a2e-13aa1706fec1

[81733f1744b1f31](https://github.com/bsorrentino/maven-confluence-plugin/commit/81733f1744b1f31) bartolomeo.sorrentino *2011-11-21 16:27:39*

**Issue 26**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@527 6a545194-3771-11de-8a2e-13aa1706fec1

[be80c834667b8e8](https://github.com/bsorrentino/maven-confluence-plugin/commit/be80c834667b8e8) bartolomeo.sorrentino *2011-11-18 09:59:18*

**upgrade release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@526 6a545194-3771-11de-8a2e-13aa1706fec1

[91ef515a8f4daec](https://github.com/bsorrentino/maven-confluence-plugin/commit/91ef515a8f4daec) bartolomeo.sorrentino@gmail.com *2011-10-20 14:18:59*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@523 6a545194-3771-11de-8a2e-13aa1706fec1

[a421a027000f063](https://github.com/bsorrentino/maven-confluence-plugin/commit/a421a027000f063) bartolomeo.sorrentino@gmail.com *2011-10-20 13:43:24*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.5-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@521 6a545194-3771-11de-8a2e-13aa1706fec1

[54bacdc959b56e9](https://github.com/bsorrentino/maven-confluence-plugin/commit/54bacdc959b56e9) bartolomeo.sorrentino@gmail.com *2011-10-20 13:43:08*

**print confluence version**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@520 6a545194-3771-11de-8a2e-13aa1706fec1

[48e8cba039e6a32](https://github.com/bsorrentino/maven-confluence-plugin/commit/48e8cba039e6a32) bartolomeo.sorrentino@gmail.com *2011-10-20 13:39:07*

**update project**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@519 6a545194-3771-11de-8a2e-13aa1706fec1

[dcd6d6f7674bcea](https://github.com/bsorrentino/maven-confluence-plugin/commit/dcd6d6f7674bcea) bartolomeo.sorrentino@gmail.com *2011-10-14 19:27:08*

**useless stuff - to be ignore**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@518 6a545194-3771-11de-8a2e-13aa1706fec1

[d7820574f4939ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/d7820574f4939ab) bartolomeo.sorrentino@gmail.com *2011-10-14 19:26:15*

**useless stuff**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@517 6a545194-3771-11de-8a2e-13aa1706fec1

[0784667d27b8080](https://github.com/bsorrentino/maven-confluence-plugin/commit/0784667d27b8080) bartolomeo.sorrentino@gmail.com *2011-10-14 19:23:42*

**fix problem - add attachment on new page**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@516 6a545194-3771-11de-8a2e-13aa1706fec1

[4bb9e49ececc327](https://github.com/bsorrentino/maven-confluence-plugin/commit/4bb9e49ececc327) bartolomeo.sorrentino@gmail.com *2011-10-14 19:18:47*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@515 6a545194-3771-11de-8a2e-13aa1706fec1

[028eb2161601638](https://github.com/bsorrentino/maven-confluence-plugin/commit/028eb2161601638) bartolomeo.sorrentino@gmail.com *2011-10-14 18:58:15*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@513 6a545194-3771-11de-8a2e-13aa1706fec1

[a1c6340d74b2611](https://github.com/bsorrentino/maven-confluence-plugin/commit/a1c6340d74b2611) bartolomeo.sorrentino@gmail.com *2011-10-14 18:55:09*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@512 6a545194-3771-11de-8a2e-13aa1706fec1

[54310a725ff7ab6](https://github.com/bsorrentino/maven-confluence-plugin/commit/54310a725ff7ab6) bartolomeo.sorrentino@gmail.com *2011-10-14 18:41:53*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@511 6a545194-3771-11de-8a2e-13aa1706fec1

[14312c68277589c](https://github.com/bsorrentino/maven-confluence-plugin/commit/14312c68277589c) bartolomeo.sorrentino@gmail.com *2011-10-14 17:13:51*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@510 6a545194-3771-11de-8a2e-13aa1706fec1

[7811dfba91672d5](https://github.com/bsorrentino/maven-confluence-plugin/commit/7811dfba91672d5) bartolomeo.sorrentino@gmail.com *2011-10-14 16:44:05*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@509 6a545194-3771-11de-8a2e-13aa1706fec1

[a3470360a58b172](https://github.com/bsorrentino/maven-confluence-plugin/commit/a3470360a58b172) bartolomeo.sorrentino@gmail.com *2011-10-14 16:43:01*

**Issue 25**

 * Detect the Confluence version
 * move to remote api version 2
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@508 6a545194-3771-11de-8a2e-13aa1706fec1

[905b300f51d7fde](https://github.com/bsorrentino/maven-confluence-plugin/commit/905b300f51d7fde) bartolomeo.sorrentino@gmail.com *2011-10-14 16:40:18*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@502 6a545194-3771-11de-8a2e-13aa1706fec1

[fc8ed3c583a795f](https://github.com/bsorrentino/maven-confluence-plugin/commit/fc8ed3c583a795f) bartolomeo.sorrentino@gmail.com *2011-10-05 13:44:56*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.4-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@500 6a545194-3771-11de-8a2e-13aa1706fec1

[e442025c61a56b3](https://github.com/bsorrentino/maven-confluence-plugin/commit/e442025c61a56b3) bartolomeo.sorrentino@gmail.com *2011-10-05 13:43:58*

**Issue 23 - Fixed**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@499 6a545194-3771-11de-8a2e-13aa1706fec1

[7300a494fef5e8c](https://github.com/bsorrentino/maven-confluence-plugin/commit/7300a494fef5e8c) bartolomeo.sorrentino@gmail.com *2011-10-04 16:55:51*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@492 6a545194-3771-11de-8a2e-13aa1706fec1

[2625d95d62a1824](https://github.com/bsorrentino/maven-confluence-plugin/commit/2625d95d62a1824) bartolomeo.sorrentino *2011-09-29 21:28:55*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.3-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@490 6a545194-3771-11de-8a2e-13aa1706fec1

[2187c6126c98298](https://github.com/bsorrentino/maven-confluence-plugin/commit/2187c6126c98298) bartolomeo.sorrentino *2011-09-29 21:27:17*

**Issue 22**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@489 6a545194-3771-11de-8a2e-13aa1706fec1

[930033b664ac32a](https://github.com/bsorrentino/maven-confluence-plugin/commit/930033b664ac32a) bartolomeo.sorrentino@gmail.com *2011-09-27 13:29:09*

**Issue 21**

 * increase diagnosis stuff
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@485 6a545194-3771-11de-8a2e-13aa1706fec1

[e9732726fc3bb7e](https://github.com/bsorrentino/maven-confluence-plugin/commit/e9732726fc3bb7e) bartolomeo.sorrentino@gmail.com *2011-08-17 17:58:32*

**add required for parameter password**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@484 6a545194-3771-11de-8a2e-13aa1706fec1

[7a90d3dff2fde26](https://github.com/bsorrentino/maven-confluence-plugin/commit/7a90d3dff2fde26) bartolomeo.sorrentino *2011-08-16 09:45:21*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@483 6a545194-3771-11de-8a2e-13aa1706fec1

[d31c8c80be20cc5](https://github.com/bsorrentino/maven-confluence-plugin/commit/d31c8c80be20cc5) bartolomeo.sorrentino@gmail.com *2011-08-05 17:23:07*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.2-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@481 6a545194-3771-11de-8a2e-13aa1706fec1

[f73a488331e1dcf](https://github.com/bsorrentino/maven-confluence-plugin/commit/f73a488331e1dcf) bartolomeo.sorrentino@gmail.com *2011-08-05 17:22:47*

**update project classpath**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@480 6a545194-3771-11de-8a2e-13aa1706fec1

[ae8211810c27a95](https://github.com/bsorrentino/maven-confluence-plugin/commit/ae8211810c27a95) bartolomeo.sorrentino@gmail.com *2011-07-27 18:33:29*

**update project settings**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@479 6a545194-3771-11de-8a2e-13aa1706fec1

[347114ab633dd90](https://github.com/bsorrentino/maven-confluence-plugin/commit/347114ab633dd90) bartolomeo.sorrentino@gmail.com *2011-07-27 18:33:06*

**update documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@478 6a545194-3771-11de-8a2e-13aa1706fec1

[2d6dfe6907a7d4b](https://github.com/bsorrentino/maven-confluence-plugin/commit/2d6dfe6907a7d4b) bartolomeo.sorrentino@gmail.com *2011-07-27 18:32:41*

**update dependencies (using versions:plugin)**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@473 6a545194-3771-11de-8a2e-13aa1706fec1

[21ae613d9642870](https://github.com/bsorrentino/maven-confluence-plugin/commit/21ae613d9642870) bartolomeo.sorrentino@gmail.com *2011-06-20 14:09:20*

**disable anchor implementation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@472 6a545194-3771-11de-8a2e-13aa1706fec1

[c27964feb6bf53b](https://github.com/bsorrentino/maven-confluence-plugin/commit/c27964feb6bf53b) bartolomeo.sorrentino@gmail.com *2011-06-20 14:06:12*

**Fix Issue 20**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@471 6a545194-3771-11de-8a2e-13aa1706fec1

[d6f0eb69f0dd583](https://github.com/bsorrentino/maven-confluence-plugin/commit/d6f0eb69f0dd583) bartolomeo.sorrentino@gmail.com *2011-06-20 14:05:19*

**new documentation url**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@470 6a545194-3771-11de-8a2e-13aa1706fec1

[aca7e526904fac7](https://github.com/bsorrentino/maven-confluence-plugin/commit/aca7e526904fac7) bartolomeo.sorrentino *2011-06-20 10:20:43*

**add release profile to improve release process**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@461 6a545194-3771-11de-8a2e-13aa1706fec1

[4ff796d10560806](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ff796d10560806) bartolomeo.sorrentino@gmail.com *2011-06-18 11:23:45*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@457 6a545194-3771-11de-8a2e-13aa1706fec1

[ace9eef08fcafdf](https://github.com/bsorrentino/maven-confluence-plugin/commit/ace9eef08fcafdf) bartolomeo.sorrentino *2011-06-18 10:44:59*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@455 6a545194-3771-11de-8a2e-13aa1706fec1

[7d4c692be41e27d](https://github.com/bsorrentino/maven-confluence-plugin/commit/7d4c692be41e27d) bartolomeo.sorrentino *2011-06-18 10:44:38*

**fix issue 18**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@454 6a545194-3771-11de-8a2e-13aa1706fec1

[cc6f1ee2c6427bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/cc6f1ee2c6427bb) bartolomeo.sorrentino *2011-06-18 10:43:49*

**fix issue 18**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@453 6a545194-3771-11de-8a2e-13aa1706fec1

[da634229ee98bbc](https://github.com/bsorrentino/maven-confluence-plugin/commit/da634229ee98bbc) bartolomeo.sorrentino *2011-06-18 10:41:27*

**fix issue 18**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@452 6a545194-3771-11de-8a2e-13aa1706fec1

[cdf14860f94c639](https://github.com/bsorrentino/maven-confluence-plugin/commit/cdf14860f94c639) bartolomeo.sorrentino *2011-06-18 10:29:40*

**fix deploy problem**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@448 6a545194-3771-11de-8a2e-13aa1706fec1

[90ada39fbf30bc6](https://github.com/bsorrentino/maven-confluence-plugin/commit/90ada39fbf30bc6) bartolomeo.sorrentino@gmail.com *2011-06-17 19:49:49*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@447 6a545194-3771-11de-8a2e-13aa1706fec1

[96ecda7349fc932](https://github.com/bsorrentino/maven-confluence-plugin/commit/96ecda7349fc932) bartolomeo.sorrentino@gmail.com *2011-06-17 17:19:50*

**[maven-release-plugin] prepare release maven-confluence-parent-3.0.0-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@445 6a545194-3771-11de-8a2e-13aa1706fec1

[fced6b67fce846a](https://github.com/bsorrentino/maven-confluence-plugin/commit/fced6b67fce846a) bartolomeo.sorrentino@gmail.com *2011-06-17 17:19:30*

**add project infos**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@444 6a545194-3771-11de-8a2e-13aa1706fec1

[e5eff9978ddcbf0](https://github.com/bsorrentino/maven-confluence-plugin/commit/e5eff9978ddcbf0) bartolomeo.sorrentino@gmail.com *2011-06-17 16:45:50*

**Issue 17 completed**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@443 6a545194-3771-11de-8a2e-13aa1706fec1

[d6d5608941f1ef9](https://github.com/bsorrentino/maven-confluence-plugin/commit/d6d5608941f1ef9) bartolomeo.sorrentino@gmail.com *2011-06-17 10:42:00*

**Issue 17 completed**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@442 6a545194-3771-11de-8a2e-13aa1706fec1

[0c8c39654ff073b](https://github.com/bsorrentino/maven-confluence-plugin/commit/0c8c39654ff073b) bartolomeo.sorrentino@gmail.com *2011-06-17 10:41:32*

**Issue 17 completed**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@441 6a545194-3771-11de-8a2e-13aa1706fec1

[6f312ca3e7bbb9f](https://github.com/bsorrentino/maven-confluence-plugin/commit/6f312ca3e7bbb9f) bartolomeo.sorrentino@gmail.com *2011-06-17 10:40:19*

**fix issue 16**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@438 6a545194-3771-11de-8a2e-13aa1706fec1

[d355b62d91df81f](https://github.com/bsorrentino/maven-confluence-plugin/commit/d355b62d91df81f) bartolomeo.sorrentino *2011-04-05 19:06:10*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=15**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@392 6a545194-3771-11de-8a2e-13aa1706fec1

[a3d6412de1fa924](https://github.com/bsorrentino/maven-confluence-plugin/commit/a3d6412de1fa924) bartolomeo.sorrentino *2011-02-12 15:23:58*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=15**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@369 6a545194-3771-11de-8a2e-13aa1706fec1

[70d7d9b068dba1c](https://github.com/bsorrentino/maven-confluence-plugin/commit/70d7d9b068dba1c) bartolomeo.sorrentino *2011-02-12 15:10:24*

**[maven-release-plugin] prepare release maven-confluence-1.3.x**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@368 6a545194-3771-11de-8a2e-13aa1706fec1

[4d0ad7e4613fb15](https://github.com/bsorrentino/maven-confluence-plugin/commit/4d0ad7e4613fb15) bartolomeo.sorrentino *2011-02-10 00:44:48*

**[maven-release-plugin] prepare branch maven-confluence-1.3.x**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@366 6a545194-3771-11de-8a2e-13aa1706fec1

[32fe5f32c1f3fa1](https://github.com/bsorrentino/maven-confluence-plugin/commit/32fe5f32c1f3fa1) bartolomeo.sorrentino *2011-02-10 00:44:29*

**prepare for branch**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@365 6a545194-3771-11de-8a2e-13aa1706fec1

[60b39de24c539de](https://github.com/bsorrentino/maven-confluence-plugin/commit/60b39de24c539de) bartolomeo.sorrentino *2011-02-10 00:42:15*

**fix java.net maven repo**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@340 6a545194-3771-11de-8a2e-13aa1706fec1

[26416ef32ec5b4b](https://github.com/bsorrentino/maven-confluence-plugin/commit/26416ef32ec5b4b) bartolomeo.sorrentino *2011-02-09 23:55:49*

**prepare for release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@339 6a545194-3771-11de-8a2e-13aa1706fec1

[808c5dba9a08dc3](https://github.com/bsorrentino/maven-confluence-plugin/commit/808c5dba9a08dc3) bartolomeo.sorrentino *2011-02-09 23:35:52*

**update**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@338 6a545194-3771-11de-8a2e-13aa1706fec1

[fa368c9e67cebd2](https://github.com/bsorrentino/maven-confluence-plugin/commit/fa368c9e67cebd2) bartolomeo.sorrentino *2010-10-31 20:34:01*

**update**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@337 6a545194-3771-11de-8a2e-13aa1706fec1

[f03033bdbb6a4ab](https://github.com/bsorrentino/maven-confluence-plugin/commit/f03033bdbb6a4ab) bartolomeo.sorrentino *2010-10-31 20:33:46*

**update**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@336 6a545194-3771-11de-8a2e-13aa1706fec1

[5f84537c8365496](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f84537c8365496) bartolomeo.sorrentino *2010-10-31 20:33:21*

**remove maven3 deprecations**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@314 6a545194-3771-11de-8a2e-13aa1706fec1

[5eed8b420580af1](https://github.com/bsorrentino/maven-confluence-plugin/commit/5eed8b420580af1) bartolomeo.sorrentino *2010-10-21 11:15:20*

**fix on multi-attachment management**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@310 6a545194-3771-11de-8a2e-13aa1706fec1

[cf0262f03ebada4](https://github.com/bsorrentino/maven-confluence-plugin/commit/cf0262f03ebada4) bartolomeo.sorrentino *2010-10-21 10:59:26*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=8**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@309 6a545194-3771-11de-8a2e-13aa1706fec1

[719c972d7450da7](https://github.com/bsorrentino/maven-confluence-plugin/commit/719c972d7450da7) bartolomeo.sorrentino *2010-10-16 18:29:50*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=8**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@308 6a545194-3771-11de-8a2e-13aa1706fec1

[ef04f33dcfb4a45](https://github.com/bsorrentino/maven-confluence-plugin/commit/ef04f33dcfb4a45) bartolomeo.sorrentino *2010-10-16 18:27:35*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@263 6a545194-3771-11de-8a2e-13aa1706fec1

[04393e5334e7176](https://github.com/bsorrentino/maven-confluence-plugin/commit/04393e5334e7176) bartolomeo.sorrentino *2010-08-11 10:54:08*

**[maven-release-plugin] prepare release maven-confluence-parent-1.3.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@261 6a545194-3771-11de-8a2e-13aa1706fec1

[f31e45698c3026c](https://github.com/bsorrentino/maven-confluence-plugin/commit/f31e45698c3026c) bartolomeo.sorrentino *2010-08-11 10:52:44*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=14**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@258 6a545194-3771-11de-8a2e-13aa1706fec1

[c0d3888b6d75bea](https://github.com/bsorrentino/maven-confluence-plugin/commit/c0d3888b6d75bea) bartolomeo.sorrentino *2010-08-11 10:28:13*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=14**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@256 6a545194-3771-11de-8a2e-13aa1706fec1

[906d488417bf78c](https://github.com/bsorrentino/maven-confluence-plugin/commit/906d488417bf78c) bartolomeo.sorrentino *2010-08-11 10:21:28*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@252 6a545194-3771-11de-8a2e-13aa1706fec1

[8fbeb1aee4094ea](https://github.com/bsorrentino/maven-confluence-plugin/commit/8fbeb1aee4094ea) bartolomeo.sorrentino *2010-06-02 13:51:53*

**[maven-release-plugin] prepare release maven-confluence-parent-1.3-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@250 6a545194-3771-11de-8a2e-13aa1706fec1

[40c4dfc5e02431c](https://github.com/bsorrentino/maven-confluence-plugin/commit/40c4dfc5e02431c) bartolomeo.sorrentino *2010-06-02 13:48:40*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=11**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@249 6a545194-3771-11de-8a2e-13aa1706fec1

[6ffcd2195e80bdb](https://github.com/bsorrentino/maven-confluence-plugin/commit/6ffcd2195e80bdb) bartolomeo.sorrentino *2010-06-02 13:34:55*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=11**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@248 6a545194-3771-11de-8a2e-13aa1706fec1

[84391f1aed916b1](https://github.com/bsorrentino/maven-confluence-plugin/commit/84391f1aed916b1) bartolomeo.sorrentino *2010-06-02 13:33:08*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@247 6a545194-3771-11de-8a2e-13aa1706fec1**


[556af3f7b9984a8](https://github.com/bsorrentino/maven-confluence-plugin/commit/556af3f7b9984a8) bartolomeo.sorrentino *2010-05-27 19:10:26*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@245 6a545194-3771-11de-8a2e-13aa1706fec1**


[3ed546baaa562ef](https://github.com/bsorrentino/maven-confluence-plugin/commit/3ed546baaa562ef) bartolomeo.sorrentino *2010-05-27 18:49:45*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@244 6a545194-3771-11de-8a2e-13aa1706fec1**


[e55257a38c86b06](https://github.com/bsorrentino/maven-confluence-plugin/commit/e55257a38c86b06) bartolomeo.sorrentino *2010-05-27 18:49:25*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@243 6a545194-3771-11de-8a2e-13aa1706fec1**


[a243216afc73b7d](https://github.com/bsorrentino/maven-confluence-plugin/commit/a243216afc73b7d) bartolomeo.sorrentino *2010-05-27 18:39:37*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@242 6a545194-3771-11de-8a2e-13aa1706fec1

[5a122b94948a3ff](https://github.com/bsorrentino/maven-confluence-plugin/commit/5a122b94948a3ff) bartolomeo.sorrentino *2010-05-20 09:41:22*

**[maven-release-plugin] prepare release maven-confluence-parent-1.2.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@240 6a545194-3771-11de-8a2e-13aa1706fec1

[a31a598b2f07297](https://github.com/bsorrentino/maven-confluence-plugin/commit/a31a598b2f07297) bartolomeo.sorrentino *2010-05-20 09:40:04*

**fix scm url**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@239 6a545194-3771-11de-8a2e-13aa1706fec1

[c81402fc0b354ea](https://github.com/bsorrentino/maven-confluence-plugin/commit/c81402fc0b354ea) bartolomeo.sorrentino *2010-05-20 09:33:51*

**add java.net deployment repo**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@238 6a545194-3771-11de-8a2e-13aa1706fec1

[c852cae9a509c94](https://github.com/bsorrentino/maven-confluence-plugin/commit/c852cae9a509c94) bartolomeo.sorrentino *2010-05-20 09:31:36*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=9**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@237 6a545194-3771-11de-8a2e-13aa1706fec1

[cfb000d138f5da1](https://github.com/bsorrentino/maven-confluence-plugin/commit/cfb000d138f5da1) bartolomeo.sorrentino *2010-05-20 09:29:34*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=9**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@236 6a545194-3771-11de-8a2e-13aa1706fec1

[d51b21c6cd1829b](https://github.com/bsorrentino/maven-confluence-plugin/commit/d51b21c6cd1829b) bartolomeo.sorrentino *2010-05-20 09:28:10*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=9**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@235 6a545194-3771-11de-8a2e-13aa1706fec1

[fd6877415626473](https://github.com/bsorrentino/maven-confluence-plugin/commit/fd6877415626473) bartolomeo.sorrentino *2010-05-20 09:27:06*

**arrange for issue 8**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@234 6a545194-3771-11de-8a2e-13aa1706fec1

[90e4dc817363f99](https://github.com/bsorrentino/maven-confluence-plugin/commit/90e4dc817363f99) bartolomeo.sorrentino *2010-05-20 09:26:24*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@200 6a545194-3771-11de-8a2e-13aa1706fec1

[528aee18d744e24](https://github.com/bsorrentino/maven-confluence-plugin/commit/528aee18d744e24) bartolomeo.sorrentino *2010-03-31 15:51:07*

**[maven-release-plugin] prepare release maven-confluence-parent-1.2**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@198 6a545194-3771-11de-8a2e-13aa1706fec1

[f5650c1f030bb92](https://github.com/bsorrentino/maven-confluence-plugin/commit/f5650c1f030bb92) bartolomeo.sorrentino *2010-03-31 15:49:36*

**update**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@184 6a545194-3771-11de-8a2e-13aa1706fec1

[7541b435690326b](https://github.com/bsorrentino/maven-confluence-plugin/commit/7541b435690326b) bartolomeo.sorrentino *2010-03-31 15:29:50*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=7**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@183 6a545194-3771-11de-8a2e-13aa1706fec1

[5457baad1ccc9d7](https://github.com/bsorrentino/maven-confluence-plugin/commit/5457baad1ccc9d7) bartolomeo.sorrentino *2010-03-31 15:28:24*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=7**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@182 6a545194-3771-11de-8a2e-13aa1706fec1

[5f11691519124d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/5f11691519124d4) bartolomeo.sorrentino *2010-03-31 15:27:39*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@176 6a545194-3771-11de-8a2e-13aa1706fec1

[7f2c120a98ff5a6](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f2c120a98ff5a6) bartolomeo.sorrentino *2009-12-22 17:32:50*

**[maven-release-plugin] prepare release maven-confluence-parent-1.1-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@174 6a545194-3771-11de-8a2e-13aa1706fec1

[484b0131c65b118](https://github.com/bsorrentino/maven-confluence-plugin/commit/484b0131c65b118) bartolomeo.sorrentino *2009-12-22 17:32:22*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@160 6a545194-3771-11de-8a2e-13aa1706fec1

[6910f731012c3c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/6910f731012c3c1) bartolomeo.sorrentino *2009-12-22 17:28:39*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@159 6a545194-3771-11de-8a2e-13aa1706fec1

[45cb9eb99d313f8](https://github.com/bsorrentino/maven-confluence-plugin/commit/45cb9eb99d313f8) bartolomeo.sorrentino *2009-12-22 17:24:19*

**add documentation**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@158 6a545194-3771-11de-8a2e-13aa1706fec1

[6978cfb5c1538e2](https://github.com/bsorrentino/maven-confluence-plugin/commit/6978cfb5c1538e2) bartolomeo.sorrentino *2009-12-22 17:17:29*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=5**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@157 6a545194-3771-11de-8a2e-13aa1706fec1

[d203c6001d34371](https://github.com/bsorrentino/maven-confluence-plugin/commit/d203c6001d34371) bartolomeo.sorrentino *2009-12-22 09:24:41*

**update dependencies**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@156 6a545194-3771-11de-8a2e-13aa1706fec1

[05f8b52cd06b0af](https://github.com/bsorrentino/maven-confluence-plugin/commit/05f8b52cd06b0af) bartolomeo.sorrentino *2009-12-21 12:22:32*

**release 1.0**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@154 6a545194-3771-11de-8a2e-13aa1706fec1

[24c3566392384e6](https://github.com/bsorrentino/maven-confluence-plugin/commit/24c3566392384e6) bartolomeo.sorrentino *2009-11-29 16:09:39*

**[maven-release-plugin] prepare release maven-confluence-reporting-plugin-plugin-1.0.0-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@152 6a545194-3771-11de-8a2e-13aa1706fec1

[88594a302caa51d](https://github.com/bsorrentino/maven-confluence-plugin/commit/88594a302caa51d) bartolomeo.sorrentino *2009-11-29 14:06:07*

**change release of core dependency**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@145 6a545194-3771-11de-8a2e-13aa1706fec1

[c6f18bc945cc0d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6f18bc945cc0d8) bartolomeo.sorrentino *2009-11-29 14:04:01*

**add scm info**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@144 6a545194-3771-11de-8a2e-13aa1706fec1

[b9f3f8fe027ef35](https://github.com/bsorrentino/maven-confluence-plugin/commit/b9f3f8fe027ef35) bartolomeo.sorrentino *2009-11-29 14:00:57*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=5**

 * add support for template wiki
 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@143 6a545194-3771-11de-8a2e-13aa1706fec1

[52d65542c027abc](https://github.com/bsorrentino/maven-confluence-plugin/commit/52d65542c027abc) bartolomeo.sorrentino *2009-11-29 11:52:24*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=5**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@142 6a545194-3771-11de-8a2e-13aa1706fec1

[ad3d6c3cc9382ad](https://github.com/bsorrentino/maven-confluence-plugin/commit/ad3d6c3cc9382ad) bartolomeo.sorrentino *2009-11-29 11:00:50*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@141 6a545194-3771-11de-8a2e-13aa1706fec1

[eeb0a52f399106a](https://github.com/bsorrentino/maven-confluence-plugin/commit/eeb0a52f399106a) bartolomeo.sorrentino *2009-11-28 16:02:49*

**[maven-release-plugin] prepare release maven-confluence-parent-1.0-SNAPSHOT**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@139 6a545194-3771-11de-8a2e-13aa1706fec1

[fe1a2e226d5b3f7](https://github.com/bsorrentino/maven-confluence-plugin/commit/fe1a2e226d5b3f7) bartolomeo.sorrentino *2009-11-28 16:00:02*

**prepare for release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@138 6a545194-3771-11de-8a2e-13aa1706fec1

[eb63d7ccce30ed4](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb63d7ccce30ed4) bartolomeo.sorrentino@gmail.com *2009-11-28 15:59:35*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@137 6a545194-3771-11de-8a2e-13aa1706fec1**


[f388d54d98af900](https://github.com/bsorrentino/maven-confluence-plugin/commit/f388d54d98af900) bartolomeo.sorrentino@gmail.com *2009-11-28 15:57:18*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@136 6a545194-3771-11de-8a2e-13aa1706fec1**


[28d5f5fa8332f51](https://github.com/bsorrentino/maven-confluence-plugin/commit/28d5f5fa8332f51) bartolomeo.sorrentino@gmail.com *2009-11-28 15:50:07*

**[maven-release-plugin] prepare release maven-confluence-parent-1.0**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@135 6a545194-3771-11de-8a2e-13aa1706fec1

[d82298083f748ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/d82298083f748ac) bartolomeo.sorrentino *2009-11-28 15:48:18*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@134 6a545194-3771-11de-8a2e-13aa1706fec1**


[1f876b840626e85](https://github.com/bsorrentino/maven-confluence-plugin/commit/1f876b840626e85) bartolomeo.sorrentino@gmail.com *2009-11-28 15:46:15*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@133 6a545194-3771-11de-8a2e-13aa1706fec1**


[c06bbbc9ef1d2be](https://github.com/bsorrentino/maven-confluence-plugin/commit/c06bbbc9ef1d2be) bartolomeo.sorrentino@gmail.com *2009-11-28 15:45:23*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@132 6a545194-3771-11de-8a2e-13aa1706fec1**


[a27b9e2ee64b992](https://github.com/bsorrentino/maven-confluence-plugin/commit/a27b9e2ee64b992) bartolomeo.sorrentino@gmail.com *2009-11-28 15:44:58*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@131 6a545194-3771-11de-8a2e-13aa1706fec1**


[25c85f00085c21e](https://github.com/bsorrentino/maven-confluence-plugin/commit/25c85f00085c21e) bartolomeo.sorrentino@gmail.com *2009-11-28 15:44:26*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@130 6a545194-3771-11de-8a2e-13aa1706fec1**


[a192e7145d27ca5](https://github.com/bsorrentino/maven-confluence-plugin/commit/a192e7145d27ca5) bartolomeo.sorrentino@gmail.com *2009-11-28 15:43:38*

**add tag information**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@129 6a545194-3771-11de-8a2e-13aa1706fec1

[a35a1c59edc3b39](https://github.com/bsorrentino/maven-confluence-plugin/commit/a35a1c59edc3b39) bartolomeo.sorrentino *2009-11-28 15:39:21*

**add tag information**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@128 6a545194-3771-11de-8a2e-13aa1706fec1

[e13fafaf1f21e7f](https://github.com/bsorrentino/maven-confluence-plugin/commit/e13fafaf1f21e7f) bartolomeo.sorrentino *2009-11-28 15:38:55*

**add tag information**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@127 6a545194-3771-11de-8a2e-13aa1706fec1

[65a01ab7fb8ce59](https://github.com/bsorrentino/maven-confluence-plugin/commit/65a01ab7fb8ce59) bartolomeo.sorrentino *2009-11-28 15:38:25*

**add tag information**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@126 6a545194-3771-11de-8a2e-13aa1706fec1

[b6fc18fed6e5b80](https://github.com/bsorrentino/maven-confluence-plugin/commit/b6fc18fed6e5b80) bartolomeo.sorrentino *2009-11-28 15:36:15*

**add tag information**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@125 6a545194-3771-11de-8a2e-13aa1706fec1

[4ec7d5fe2c068c1](https://github.com/bsorrentino/maven-confluence-plugin/commit/4ec7d5fe2c068c1) bartolomeo.sorrentino *2009-11-28 15:34:55*

**before release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@124 6a545194-3771-11de-8a2e-13aa1706fec1

[586f3096d6065d4](https://github.com/bsorrentino/maven-confluence-plugin/commit/586f3096d6065d4) bartolomeo.sorrentino *2009-11-28 15:28:19*

**[maven-release-plugin] prepare release confluence-mojo-1.0**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@122 6a545194-3771-11de-8a2e-13aa1706fec1

[52dff04a3e4f716](https://github.com/bsorrentino/maven-confluence-plugin/commit/52dff04a3e4f716) bartolomeo.sorrentino *2009-11-28 15:08:39*

**changed core depenency**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@121 6a545194-3771-11de-8a2e-13aa1706fec1

[9a02295ada555e0](https://github.com/bsorrentino/maven-confluence-plugin/commit/9a02295ada555e0) bartolomeo.sorrentino *2009-11-28 15:08:09*

**[maven-release-plugin] prepare for next development iteration**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@120 6a545194-3771-11de-8a2e-13aa1706fec1

[c6071b335993e98](https://github.com/bsorrentino/maven-confluence-plugin/commit/c6071b335993e98) bartolomeo.sorrentino *2009-11-28 15:06:46*

**[maven-release-plugin] prepare release confluence-mojo-1.0**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@118 6a545194-3771-11de-8a2e-13aa1706fec1

[b7f265e944fd664](https://github.com/bsorrentino/maven-confluence-plugin/commit/b7f265e944fd664) bartolomeo.sorrentino *2009-11-28 15:06:28*

**before release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@117 6a545194-3771-11de-8a2e-13aa1706fec1

[acf06cd6f059343](https://github.com/bsorrentino/maven-confluence-plugin/commit/acf06cd6f059343) bartolomeo.sorrentino *2009-11-28 15:01:34*

**before release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@116 6a545194-3771-11de-8a2e-13aa1706fec1

[87be86eecc62be5](https://github.com/bsorrentino/maven-confluence-plugin/commit/87be86eecc62be5) bartolomeo.sorrentino *2009-11-28 14:58:32*

**before release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@115 6a545194-3771-11de-8a2e-13aa1706fec1

[99f2ceb3b70928a](https://github.com/bsorrentino/maven-confluence-plugin/commit/99f2ceb3b70928a) bartolomeo.sorrentino *2009-11-28 14:57:02*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@114 6a545194-3771-11de-8a2e-13aa1706fec1**


[e52050b1d151a11](https://github.com/bsorrentino/maven-confluence-plugin/commit/e52050b1d151a11) bartolomeo.sorrentino@gmail.com *2009-11-28 14:36:46*

**add scm infoupgrade anno-mojo release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@113 6a545194-3771-11de-8a2e-13aa1706fec1

[512306f7e677c3a](https://github.com/bsorrentino/maven-confluence-plugin/commit/512306f7e677c3a) bartolomeo.sorrentino *2009-11-28 14:12:59*

**[maven-release-plugin] prepare release confluence-mojo-1.0**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@112 6a545194-3771-11de-8a2e-13aa1706fec1

[1e966183388d15f](https://github.com/bsorrentino/maven-confluence-plugin/commit/1e966183388d15f) bartolomeo.sorrentino *2009-11-28 14:05:19*

**add scm info**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@98 6a545194-3771-11de-8a2e-13aa1706fec1

[df4cc92ba3f43b5](https://github.com/bsorrentino/maven-confluence-plugin/commit/df4cc92ba3f43b5) bartolomeo.sorrentino *2009-11-28 14:00:47*

**upgrade anno-mojo release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@97 6a545194-3771-11de-8a2e-13aa1706fec1

[4aa3a44877799c9](https://github.com/bsorrentino/maven-confluence-plugin/commit/4aa3a44877799c9) bartolomeo.sorrentino *2009-11-28 13:53:12*

**upgrade anno-mojo release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@96 6a545194-3771-11de-8a2e-13aa1706fec1

[aaafa581151a0cc](https://github.com/bsorrentino/maven-confluence-plugin/commit/aaafa581151a0cc) bartolomeo.sorrentino *2009-11-28 13:52:44*

**upgrade anno-mojo release**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@95 6a545194-3771-11de-8a2e-13aa1706fec1

[d577d38d97cbc08](https://github.com/bsorrentino/maven-confluence-plugin/commit/d577d38d97cbc08) bartolomeo.sorrentino *2009-11-28 13:52:29*

**site refinements**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@94 6a545194-3771-11de-8a2e-13aa1706fec1

[867d6a7084c8f4d](https://github.com/bsorrentino/maven-confluence-plugin/commit/867d6a7084c8f4d) bartolomeo.sorrentino *2009-11-08 17:57:10*

**upgrade version for mac snow profile**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@89 6a545194-3771-11de-8a2e-13aa1706fec1

[f7b47790118c1cd](https://github.com/bsorrentino/maven-confluence-plugin/commit/f7b47790118c1cd) bartolomeo.sorrentino *2009-11-08 16:33:40*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=4**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@83 6a545194-3771-11de-8a2e-13aa1706fec1

[4b9db4e3a807466](https://github.com/bsorrentino/maven-confluence-plugin/commit/4b9db4e3a807466) bartolomeo.sorrentino *2009-09-29 09:35:36*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=4**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@82 6a545194-3771-11de-8a2e-13aa1706fec1

[c2b6973028b81a5](https://github.com/bsorrentino/maven-confluence-plugin/commit/c2b6973028b81a5) bartolomeo.sorrentino *2009-09-29 09:34:50*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=4**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@81 6a545194-3771-11de-8a2e-13aa1706fec1

[776e5238993790c](https://github.com/bsorrentino/maven-confluence-plugin/commit/776e5238993790c) bartolomeo.sorrentino *2009-09-29 09:34:31*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=2**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@58 6a545194-3771-11de-8a2e-13aa1706fec1

[54c4690e276976d](https://github.com/bsorrentino/maven-confluence-plugin/commit/54c4690e276976d) bartolomeo.sorrentino *2009-08-06 00:35:36*

**update docs**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@46 6a545194-3771-11de-8a2e-13aa1706fec1

[9d11cd5207089d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/9d11cd5207089d8) bartolomeo.sorrentino *2009-05-09 14:45:11*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=1**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@44 6a545194-3771-11de-8a2e-13aa1706fec1

[8ca4db35ddd4339](https://github.com/bsorrentino/maven-confluence-plugin/commit/8ca4db35ddd4339) bartolomeo.sorrentino *2009-05-09 14:07:55*

**http://code.google.com/p/maven-confluence-plugin/issues/detail?id=1**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@43 6a545194-3771-11de-8a2e-13aa1706fec1

[c13ca4c8dcc3994](https://github.com/bsorrentino/maven-confluence-plugin/commit/c13ca4c8dcc3994) bartolomeo.sorrentino *2009-05-09 14:07:37*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@42 6a545194-3771-11de-8a2e-13aa1706fec1

[2b51c3e5926213c](https://github.com/bsorrentino/maven-confluence-plugin/commit/2b51c3e5926213c) bartolomeo.sorrentino *2009-05-03 16:13:14*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@40 6a545194-3771-11de-8a2e-13aa1706fec1

[1d547ab6dbc9c4f](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d547ab6dbc9c4f) bartolomeo.sorrentino *2009-05-03 16:04:09*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@35 6a545194-3771-11de-8a2e-13aa1706fec1**


[b7ed4ee3e484e57](https://github.com/bsorrentino/maven-confluence-plugin/commit/b7ed4ee3e484e57) bartolomeo.sorrentino *2009-05-03 15:13:56*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@34 6a545194-3771-11de-8a2e-13aa1706fec1**


[b11bf6c2172d9dd](https://github.com/bsorrentino/maven-confluence-plugin/commit/b11bf6c2172d9dd) bartolomeo.sorrentino *2009-05-03 15:11:38*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@33 6a545194-3771-11de-8a2e-13aa1706fec1

[7e2ba4c64b5cd23](https://github.com/bsorrentino/maven-confluence-plugin/commit/7e2ba4c64b5cd23) bartolomeo.sorrentino *2009-05-03 15:10:14*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@32 6a545194-3771-11de-8a2e-13aa1706fec1

[2e063d8795f292a](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e063d8795f292a) bartolomeo.sorrentino *2009-05-03 15:08:21*

**upgrade**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@31 6a545194-3771-11de-8a2e-13aa1706fec1

[dda411bce247d08](https://github.com/bsorrentino/maven-confluence-plugin/commit/dda411bce247d08) bartolomeo.sorrentino *2009-05-03 15:07:53*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@29 6a545194-3771-11de-8a2e-13aa1706fec1**


[6771d6634d89c78](https://github.com/bsorrentino/maven-confluence-plugin/commit/6771d6634d89c78) bartolomeo.sorrentino *2009-05-03 14:38:59*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@28 6a545194-3771-11de-8a2e-13aa1706fec1**


[52d2dfb70d573da](https://github.com/bsorrentino/maven-confluence-plugin/commit/52d2dfb70d573da) bartolomeo.sorrentino *2009-05-03 14:34:28*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@25 6a545194-3771-11de-8a2e-13aa1706fec1**


[8477dc7de24442f](https://github.com/bsorrentino/maven-confluence-plugin/commit/8477dc7de24442f) bartolomeo.sorrentino *2009-05-03 10:45:07*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@14 6a545194-3771-11de-8a2e-13aa1706fec1**


[57771c6c8e60c93](https://github.com/bsorrentino/maven-confluence-plugin/commit/57771c6c8e60c93) bartolomeo.sorrentino *2009-05-03 10:34:54*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@11 6a545194-3771-11de-8a2e-13aa1706fec1

[005bef026e29bfb](https://github.com/bsorrentino/maven-confluence-plugin/commit/005bef026e29bfb) bartolomeo.sorrentino *2009-05-02 23:56:54*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@10 6a545194-3771-11de-8a2e-13aa1706fec1

[1cec9072886e49b](https://github.com/bsorrentino/maven-confluence-plugin/commit/1cec9072886e49b) bartolomeo.sorrentino *2009-05-02 23:56:28*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@9 6a545194-3771-11de-8a2e-13aa1706fec1

[1d91cdfa1e51203](https://github.com/bsorrentino/maven-confluence-plugin/commit/1d91cdfa1e51203) bartolomeo.sorrentino *2009-05-02 23:55:33*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@8 6a545194-3771-11de-8a2e-13aa1706fec1

[eb035003fd08c45](https://github.com/bsorrentino/maven-confluence-plugin/commit/eb035003fd08c45) bartolomeo.sorrentino *2009-05-02 23:54:41*

**Initial import.**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@7 6a545194-3771-11de-8a2e-13aa1706fec1

[3866892e8e90cfa](https://github.com/bsorrentino/maven-confluence-plugin/commit/3866892e8e90cfa) bartolomeo.sorrentino *2009-05-02 23:53:21*

**git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@6 6a545194-3771-11de-8a2e-13aa1706fec1**


[699f181638de4c3](https://github.com/bsorrentino/maven-confluence-plugin/commit/699f181638de4c3) bartolomeo.sorrentino *2009-05-02 23:52:22*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@5 6a545194-3771-11de-8a2e-13aa1706fec1

[8c179015d62393b](https://github.com/bsorrentino/maven-confluence-plugin/commit/8c179015d62393b) bartolomeo.sorrentino *2009-05-02 23:44:04*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@4 6a545194-3771-11de-8a2e-13aa1706fec1

[fb84daa040d7078](https://github.com/bsorrentino/maven-confluence-plugin/commit/fb84daa040d7078) bartolomeo.sorrentino *2009-05-02 23:43:43*

**initial import**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@3 6a545194-3771-11de-8a2e-13aa1706fec1

[2f4c4deb8723fd3](https://github.com/bsorrentino/maven-confluence-plugin/commit/2f4c4deb8723fd3) bartolomeo.sorrentino *2009-05-02 23:42:43*

**Initial import.**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@2 6a545194-3771-11de-8a2e-13aa1706fec1

[1f0bf8e740c663d](https://github.com/bsorrentino/maven-confluence-plugin/commit/1f0bf8e740c663d) bartolomeo.sorrentino *2009-05-02 23:41:16*

**Initial directory structure.**

 * git-svn-id: https://maven-confluence-plugin.googlecode.com/svn/trunk@1 6a545194-3771-11de-8a2e-13aa1706fec1

[f0633df60216634](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0633df60216634) (no author) *2009-05-02 23:31:52*


## maven-confluence-parent-3.4.0
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.0**


[9752a5d5414f419](https://github.com/bsorrentino/maven-confluence-plugin/commit/9752a5d5414f419) bsorrentino *2013-08-14 14:56:41*

**[maven-release-plugin] prepare for next development iteration**


[1a0568ff2e7c114](https://github.com/bsorrentino/maven-confluence-plugin/commit/1a0568ff2e7c114) bsorrentino *2013-08-14 14:46:58*


## maven-confluence-parent-3.4.0-rc1
### Generic changes

**[maven-release-plugin] prepare release 3.4.0-rc1**


[2136f08fa60a0bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/2136f08fa60a0bb) bsorrentino *2013-08-14 14:45:38*

**[maven-release-plugin] prepare for next development iteration**


[c890bfa6831f9b6](https://github.com/bsorrentino/maven-confluence-plugin/commit/c890bfa6831f9b6) bsorrentino *2013-08-14 14:43:18*


## v3.4.0-rc1
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.0-SNAPSHOT**


[a7c79c73be31848](https://github.com/bsorrentino/maven-confluence-plugin/commit/a7c79c73be31848) bsorrentino *2013-08-14 14:41:29*

**issue 56**


[b692c0840a7eeef](https://github.com/bsorrentino/maven-confluence-plugin/commit/b692c0840a7eeef) softphone *2013-08-09 10:22:02*


## maven-confluence-parent-3.4.0-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.4.0-SNAPSHOT**


[ec721b9a4079c30](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec721b9a4079c30) bsorrentino *2013-07-27 17:14:41*

**issue 55**


[af631c3972519c7](https://github.com/bsorrentino/maven-confluence-plugin/commit/af631c3972519c7) bsorrentino *2013-07-27 17:12:30*

**issue55**


[9f86419772c6547](https://github.com/bsorrentino/maven-confluence-plugin/commit/9f86419772c6547) bsorrentino *2013-07-27 15:53:00*

**issue 55**

 * add detect &#x27;plugin project&#x27;

[96ac8e3a5ffc40e](https://github.com/bsorrentino/maven-confluence-plugin/commit/96ac8e3a5ffc40e) softphone *2013-07-27 00:38:00*

**issue 55**

 * add detect &#x27;plugin project&#x27;

[e82aa6fb7372524](https://github.com/bsorrentino/maven-confluence-plugin/commit/e82aa6fb7372524) softphone *2013-07-27 00:33:49*

**issue 55**

 * add detect &#x27;plugin project&#x27;

[373d3118573778e](https://github.com/bsorrentino/maven-confluence-plugin/commit/373d3118573778e) softphone *2013-07-27 00:30:45*

**issue 55**


[7f8a10ad33a0b1b](https://github.com/bsorrentino/maven-confluence-plugin/commit/7f8a10ad33a0b1b) softphone *2013-07-25 10:47:46*

**issue 55**


[c756e819d92f3d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/c756e819d92f3d8) softphone *2013-07-25 10:45:35*

**issue 55**


[2e774a27abb511c](https://github.com/bsorrentino/maven-confluence-plugin/commit/2e774a27abb511c) softphone *2013-07-24 01:11:56*

**issue 55**


[44d0efb6e33c396](https://github.com/bsorrentino/maven-confluence-plugin/commit/44d0efb6e33c396) softphone *2013-07-24 01:10:57*

**remove unused import**


[5747b2ac8ac7d43](https://github.com/bsorrentino/maven-confluence-plugin/commit/5747b2ac8ac7d43) bsorrentino *2013-06-14 21:39:43*

**[maven-release-plugin] prepare for next development iteration**


[f6339228a46ba59](https://github.com/bsorrentino/maven-confluence-plugin/commit/f6339228a46ba59) bsorrentino *2013-05-28 19:55:48*


## maven-confluence-parent-3.3.0-rc1
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-rc1**


[68596fa1d2e207f](https://github.com/bsorrentino/maven-confluence-plugin/commit/68596fa1d2e207f) bsorrentino *2013-05-28 19:55:39*

**update version**


[52c0dd0b1cd1b87](https://github.com/bsorrentino/maven-confluence-plugin/commit/52c0dd0b1cd1b87) bsorrentino *2013-05-28 19:55:05*

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-SNAPSHOT**


[cd9efc66d9cc937](https://github.com/bsorrentino/maven-confluence-plugin/commit/cd9efc66d9cc937) bsorrentino *2013-05-28 19:47:07*

**[maven-release-plugin] prepare for next development iteration**


[ffc3fbb300bc602](https://github.com/bsorrentino/maven-confluence-plugin/commit/ffc3fbb300bc602) bsorrentino *2013-05-28 19:31:13*


## maven-confluence-parent-3.3.0-beta4-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-beta4-SNAPSHOT**


[bb2f0af6848d7ac](https://github.com/bsorrentino/maven-confluence-plugin/commit/bb2f0af6848d7ac) bsorrentino *2013-05-28 19:31:03*

**update doc url**


[ce4e64d529d0a70](https://github.com/bsorrentino/maven-confluence-plugin/commit/ce4e64d529d0a70) softphone *2013-05-28 19:28:46*

**Issue 41**


[664fdbda2f6abb9](https://github.com/bsorrentino/maven-confluence-plugin/commit/664fdbda2f6abb9) softphone *2013-05-28 19:20:19*

**Issue 53**


[7cb89a801437589](https://github.com/bsorrentino/maven-confluence-plugin/commit/7cb89a801437589) softphone *2013-05-28 17:53:39*

**Issue 53**


[5bd0fa55e1f8e80](https://github.com/bsorrentino/maven-confluence-plugin/commit/5bd0fa55e1f8e80) bsorrentino *2013-05-28 15:26:55*

**[maven-release-plugin] prepare for next development iteration**


[48cddf528a78318](https://github.com/bsorrentino/maven-confluence-plugin/commit/48cddf528a78318) bsorrentino *2013-02-15 10:42:58*


## maven-confluence-parent-3.3.0-beta3-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-beta3-SNAPSHOT**


[6eb7eb79dedc0a7](https://github.com/bsorrentino/maven-confluence-plugin/commit/6eb7eb79dedc0a7) bsorrentino *2013-02-15 10:42:54*

**Issue 51**


[415d6507cf69f18](https://github.com/bsorrentino/maven-confluence-plugin/commit/415d6507cf69f18) softphone *2013-02-15 10:39:33*

**[maven-release-plugin] prepare for next development iteration**


[aec36dd797034be](https://github.com/bsorrentino/maven-confluence-plugin/commit/aec36dd797034be) bsorrentino *2012-12-22 15:01:53*


## maven-confluence-parent-3.3.0-beta2-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-beta2-SNAPSHOT**


[22fa226010d8505](https://github.com/bsorrentino/maven-confluence-plugin/commit/22fa226010d8505) bsorrentino *2012-12-22 15:01:47*

**directory layout refactoring**


[8beae6194bd620a](https://github.com/bsorrentino/maven-confluence-plugin/commit/8beae6194bd620a) bsorrentino *2012-12-22 14:55:45*

**directory layout refactoring**


[23c63f370d5bc46](https://github.com/bsorrentino/maven-confluence-plugin/commit/23c63f370d5bc46) bsorrentino *2012-12-22 14:55:14*

**issue 49**

 * bug fix

[881d4a0e6d817aa](https://github.com/bsorrentino/maven-confluence-plugin/commit/881d4a0e6d817aa) softphone *2012-12-21 12:45:11*

**Issue 49**


[68b167f3848298d](https://github.com/bsorrentino/maven-confluence-plugin/commit/68b167f3848298d) softphone *2012-12-20 19:52:18*

**Issue 49**


[80cacb8c86eeee0](https://github.com/bsorrentino/maven-confluence-plugin/commit/80cacb8c86eeee0) softphone *2012-12-20 19:51:54*

**integration test**


[16eda17c34c57bb](https://github.com/bsorrentino/maven-confluence-plugin/commit/16eda17c34c57bb) softphone *2012-12-18 01:11:51*

**integration test**


[ecbb7e694e43ba5](https://github.com/bsorrentino/maven-confluence-plugin/commit/ecbb7e694e43ba5) softphone *2012-12-18 01:11:27*

**[maven-release-plugin] prepare for next development iteration**


[7dd884251b8fdbf](https://github.com/bsorrentino/maven-confluence-plugin/commit/7dd884251b8fdbf) bsorrentino *2012-12-16 15:57:56*


## maven-confluence-parent-3.3.0-SNAPSHOT
### Generic changes

**[maven-release-plugin] prepare release maven-confluence-parent-3.3.0-SNAPSHOT**


[383f9ebad2ecd85](https://github.com/bsorrentino/maven-confluence-plugin/commit/383f9ebad2ecd85) bsorrentino *2012-12-16 15:57:50*

**add siteDescriptor parameter**


[8062a9abfd93af7](https://github.com/bsorrentino/maven-confluence-plugin/commit/8062a9abfd93af7) softphone *2012-12-16 15:51:49*

**add namespace to generated schema**


[f767360e558d32b](https://github.com/bsorrentino/maven-confluence-plugin/commit/f767360e558d32b) softphone *2012-12-14 01:17:16*

**3.3.0**


[f0464dba558fe74](https://github.com/bsorrentino/maven-confluence-plugin/commit/f0464dba558fe74) softphone *2012-11-01 16:40:11*

**backward compatibility test**


[727f5539477c6c7](https://github.com/bsorrentino/maven-confluence-plugin/commit/727f5539477c6c7) bsorrentino *2012-10-29 12:21:53*

**backward compatibility test**


[b3245e136da962d](https://github.com/bsorrentino/maven-confluence-plugin/commit/b3245e136da962d) bsorrentino *2012-10-29 12:20:36*

**use model to generate site**


[b84133082417001](https://github.com/bsorrentino/maven-confluence-plugin/commit/b84133082417001) bsorrentino *2012-10-23 16:55:49*

**issue 38**


[4831decab1d1622](https://github.com/bsorrentino/maven-confluence-plugin/commit/4831decab1d1622) softphone *2012-10-20 21:42:27*

**issue 38**


[631d62ae9c5333e](https://github.com/bsorrentino/maven-confluence-plugin/commit/631d62ae9c5333e) softphone *2012-10-20 21:38:15*

**issue 38**


[ec6d2771e17b635](https://github.com/bsorrentino/maven-confluence-plugin/commit/ec6d2771e17b635) softphone *2012-10-20 17:23:17*

**issue 38**


[70a93caa1db864e](https://github.com/bsorrentino/maven-confluence-plugin/commit/70a93caa1db864e) softphone *2012-10-19 17:36:11*

**issue 38**


[560b31a8ba5b8d6](https://github.com/bsorrentino/maven-confluence-plugin/commit/560b31a8ba5b8d6) softphone *2012-10-18 23:43:36*

**issue 38**


[78952d793b8bf8e](https://github.com/bsorrentino/maven-confluence-plugin/commit/78952d793b8bf8e) bsorrentino *2012-10-18 17:41:44*

**issue 38**


[9ce375ebb7c40f4](https://github.com/bsorrentino/maven-confluence-plugin/commit/9ce375ebb7c40f4) bsorrentino *2012-10-18 17:39:53*

**update version**


[8f8d52b61fef324](https://github.com/bsorrentino/maven-confluence-plugin/commit/8f8d52b61fef324) softphone *2012-10-13 20:01:02*

**update version**


[e2fa4bb96384f78](https://github.com/bsorrentino/maven-confluence-plugin/commit/e2fa4bb96384f78) softphone *2012-10-13 20:00:52*

**jaxb poc**


[370064b6ea246d8](https://github.com/bsorrentino/maven-confluence-plugin/commit/370064b6ea246d8) softphone *2012-10-13 19:59:57*


