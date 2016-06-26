/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.confluence;

/**
 *
 * @author bsorrentino
 */
public enum ExportFormat {
    
        PDF("spaces/flyingpdf/pdfpageexport.action" ), DOC("exportword");

        public String url;

        private ExportFormat(String url) {
            this.url = url;
        }
    
}
