# Image ref link test

* add an absolute ![conf-icon](http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png "My conf-icon") with title.
* add a relative ![conf-icon](../../../../maven-confluence-reporting-plugin/src/test/resources/conf-icon-64.png "My conf-icon") with title.
* add a relative ![conf-icon](../../../../maven-confluence-reporting-plugin/src/test/resources/conf-icon-64.png) without title.
* add a ref img ![conf-icon-y][y] with title.
* add a ref img ![conf-icon-y1][y1] without title.
* add a ref img ![conf-icon-y2][y2] relative.
* add a ref img ![conf-icon-none] relative with default refname.

[y]: http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png "My conf-icon"
[y1]: http://www.lewe.com/wp-content/uploads/2016/03/conf-icon-64.png
[y2]: ../../../../maven-confluence-reporting-plugin/src/test/resources/conf-icon-64.png
[conf-icon-none]: ../../../../maven-confluence-reporting-plugin/src/test/resources/conf-icon-64.png

