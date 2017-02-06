import markdown = require("marked");

class WikiRenderer implements MarkedRenderer {

    langs = {
        'actionscript3' :true,
        'bash'          :true,
        'csharp'        :true,
        'coldfusion'    :true, 
        'cpp'           :true, 
        'css'           :true, 
        'delphi'        :true, 
        'diff'          :true,
        'erlang'        :true,
        'groovy'        :true,
        'java'          :true,
        'javafx'        :true,
        'javascript'    :true,
        'perl'          :true,
        'php'           :true,
        'none'          :true,
        'powershell'    :true,
        'python'        :true,
        'ruby'          :true,
        'scala'         :true,
        'sql'           :true,
        'vb'            :true,
        'html'          :true,
        'xml'           :true
    };

	paragraph(text) { return text + '\n\n'; }
	
    html(html:string) { return html; }

	heading(text, level, raw) { return 'h' + level + '. ' + text + '\n\n' }

	strong(text) { return '*' + text + '*' }

	em(text) { return '_' + text + '_' }

	del(text) { return '-' + text + '-' }

	codespan(text) { return '{{' + text + '}}' }

	blockquote(quote) { return '{quote}' + quote + '{quote}' }

	br() { return '\n' }

	hr() { return '----' }

	link(href, title, text) {
		let arr = [text,href];
		if (title) arr.push(title);
		
		return '[' + arr.join('|') + ']'
	}

	list(body:string, ordered) {
        let arr = body.trim().split('\n').filter( (line) => line );

		var type = ordered ? '#' : '*'
        return arr.map( (line) => type + ' ' + line ).join('\n') + '\n\n'

	}

	listitem(body:string /*, ordered*/) { return body + '\n' }

	image(href, title, text) { return '!' + href + '!'}

	table(header, body) { return header + body + '\n' }

	tablerow(content /*, flags*/) { return content + '\n' }

	tablecell(content:string, flags) {
		var type = flags.header ? '||' : '|'
		return type + content;
	}

	code(code:string, lang:string) {
		lang = this.langs[lang] ? lang :  "";

		return '{code:' + lang + '}\n' + code + '\n{code}\n\n';
	}

    text(text: string): string { return text; }
}

const  renderer = new WikiRenderer()

export function markdown2wiki(md:string|Buffer) {
	return markdown(md.toString(), {
        renderer: renderer,
        sanitize:true // Sanitize the output. Ignore any HTML that has been input.
    });
}



