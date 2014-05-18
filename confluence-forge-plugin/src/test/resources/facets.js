/**
 * 
 */
/*
		"mvn": java.lang.Class.forName('org.jboss.forge.addon.maven.projects.MavenFacet'),
		"mvnPlugin": java.lang.Class.forName('org.jboss.forge.addon.maven.projects.MavenPluginFacet')
*/

//exports.facets = 
module.exports = 
function() {
	
	
	var facets = {};
	
	if( typeof project == "undefined" ){	
		print( "WARN: project is undefined!");
		return facets;
	}
	
	var pattern = /^([A-Za-z]+)/;
		
	var i = project.getFacets().iterator();
		
	while( i.hasNext() ) {
			
			var facet = i.next(), 
				n = facet.class.simpleName, 
				g = pattern.exec(n); 
			if( g ) {
				facets[g[0].replace("Impl", "").toLowerCase()] = facet;
			}else print( "WARN: " + n + ",DOESN'T MATCH ");
			
	}	
	
	return facets;
	
}