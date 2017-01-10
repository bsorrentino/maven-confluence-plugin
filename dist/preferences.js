"use strict";
var Preferences = require("preferences");
var init = {
    account: { username: "bartolo", password: "password" },
    data: {
        url: "http://localhost",
        cycle: 0
    }
};
var prefs = new Preferences("org.bsc.test", init);
prefs.data.cycle++;
console.dir(prefs);
