"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Preferences = require("preferences");
var prefs = new Preferences("org.bsc.test", {
    cycle: 0,
    confluence: {
        password: "mypassword",
    }
});
prefs.cycle++;
console.dir(prefs);
