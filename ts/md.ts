import markdown = require("marked");

class WikiRenderer implements markdown.Renderer {

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

	paragraph(text:string) { return text + '\n\n'; }
	
    html(html:string) { return html; }

	heading(text:string, level:number, raw:string) { return 'h' + level + '. ' + text + '\n\n' }

	strong(text:string) { return '*' + text + '*' }

	em(text:string) { return '_' + text + '_' }

	del(text:string) { return '-' + text + '-' }

	codespan(text:string) { return '{{' + text + '}}' }

	blockquote(quote:string) { return '{quote}' + quote + '{quote}' }

	br() { return '\n' }

	hr() { return '----' }

	link(href:string, title:string, text:string) {
		let arr = [text,href];
		if (title) arr.push(title);
		
		return '[' + arr.join('|') + ']'
	}

	list(body:string, ordered:boolean) {
        let arr = body.trim().split('\n').filter( (line) => line );

		var type = ordered ? '#' : '*'
        return arr.map( (line) => type + ' ' + line ).join('\n') + '\n\n'

	}

	listitem(body:string /*, ordered*/) { return body + '\n' }

	image(href:string, title:string, text:string) { return '!' + href + '!'}

	table(header:string, body:string) { return header + body + '\n' }

	tablerow(content:string /*, flags*/) { return content + '\n' }

	tablecell(content:string, flags:any) {
		var type = flags.header ? '||' : '|'
		return type + content;
	}

	code(code:string, lang:string) {
		lang = (<any>this.langs)[lang] ? lang :  "";

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



