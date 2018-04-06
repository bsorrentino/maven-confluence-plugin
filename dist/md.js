"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var markdown = require("marked");
var WikiRenderer = /** @class */ (function () {
    function WikiRenderer() {
        this.langs = {
            'actionscript3': true,
            'bash': true,
            'csharp': true,
            'coldfusion': true,
            'cpp': true,
            'css': true,
            'delphi': true,
            'diff': true,
            'erlang': true,
            'groovy': true,
            'java': true,
            'javafx': true,
            'javascript': true,
            'perl': true,
            'php': true,
            'none': true,
            'powershell': true,
            'python': true,
            'ruby': true,
            'scala': true,
            'sql': true,
            'vb': true,
            'html': true,
            'xml': true
        };
    }
    WikiRenderer.prototype.paragraph = function (text) { return text + '\n\n'; };
    WikiRenderer.prototype.html = function (html) { return html; };
    WikiRenderer.prototype.heading = function (text, level, raw) { return 'h' + level + '. ' + text + '\n\n'; };
    WikiRenderer.prototype.strong = function (text) { return '*' + text + '*'; };
    WikiRenderer.prototype.em = function (text) { return '_' + text + '_'; };
    WikiRenderer.prototype.del = function (text) { return '-' + text + '-'; };
    WikiRenderer.prototype.codespan = function (text) { return '{{' + text + '}}'; };
    WikiRenderer.prototype.blockquote = function (quote) { return '{quote}' + quote + '{quote}'; };
    WikiRenderer.prototype.br = function () { return '\n'; };
    WikiRenderer.prototype.hr = function () { return '----'; };
    WikiRenderer.prototype.link = function (href, title, text) {
        var arr = [text, href];
        if (title)
            arr.push(title);
        return '[' + arr.join('|') + ']';
    };
    WikiRenderer.prototype.list = function (body, ordered) {
        var arr = body.trim().split('\n').filter(function (line) { return line; });
        var type = ordered ? '#' : '*';
        return arr.map(function (line) { return type + ' ' + line; }).join('\n') + '\n\n';
    };
    WikiRenderer.prototype.listitem = function (body /*, ordered*/) { return body + '\n'; };
    WikiRenderer.prototype.image = function (href, title, text) { return '!' + href + '!'; };
    WikiRenderer.prototype.table = function (header, body) { return header + body + '\n'; };
    WikiRenderer.prototype.tablerow = function (content /*, flags*/) { return content + '\n'; };
    WikiRenderer.prototype.tablecell = function (content, flags) {
        var type = flags.header ? '||' : '|';
        return type + content;
    };
    WikiRenderer.prototype.code = function (code, lang) {
        lang = this.langs[lang] ? lang : "";
        return '{code:' + lang + '}\n' + code + '\n{code}\n\n';
    };
    WikiRenderer.prototype.text = function (text) { return text; };
    return WikiRenderer;
}());
var renderer = new WikiRenderer();
function markdown2wiki(md) {
    return markdown(md.toString(), {
        renderer: renderer,
        sanitize: true // Sanitize the output. Ignore any HTML that has been input.
    });
}
exports.markdown2wiki = markdown2wiki;
