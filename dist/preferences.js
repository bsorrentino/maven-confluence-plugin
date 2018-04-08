"use strict";
/// <reference path="preferences.d.ts" />
Object.defineProperty(exports, "__esModule", { value: true });
const Preferences = require("preferences");
let prefs = new Preferences("org.bsc.test", {
    cycle: 0,
    confluence: {
        password: "mypassword",
    }
});
prefs.cycle++;
console.dir(prefs);
