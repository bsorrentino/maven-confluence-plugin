
import {rxSite} from "./confluence-site";
import * as path from "path";
//import Rx = require("rx");

rxSite(path.join(__dirname,'site.xml'))
  .subscribe( (data) => {
      console.log( "element", data.$.name || data.$.uri, data.type);
    });
