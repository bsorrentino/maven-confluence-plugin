
var Client = require('xmlrpc-lite');

var t = new Client('http://localhost:8090/rpc/xmlrpc');
t.call('login', {
  user: 'admin',
  passwd: 'admin'
}, function(err, xml) {
  if(err) throw err;
  console.log(xml);
});

console.log( "end!");

// 1) token = (String) call("login", username, password);
// 2) Object[] args = { arg1, arg2 }; return call(command, args);
// 3) return call( 'confluence2.|confluence2.', command, args );
