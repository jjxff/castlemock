grammar Expression;


expression
    : '${' type=expressionName
       ('(' (WS+)? (arguments+=argument (',' (WS+)? arguments+=argument )*)? ')')? '}'
     ;

expressionName
    : value=(CHAR | DIGIT | UNDER_SCORE)+
    ;

argument
    : name=argumentName
    '=' value=argumentValue
    ;

argumentName
    : value=CHAR ((CHAR| DIGIT | UNDER_SCORE)+)?
    ;


argumentValue
    : (argumentNumber | argumentString | array)
    ;


argumentString
    : '"' (value=string)? '"'
    ;

argumentNumber
    : value=number
    ;

number
    : value=DIGIT+ ('.' DIGIT+)?
    ;

array
    :
    ('[' (WS+)? (value+=argumentValue ( ',' (WS+)? value+=argumentValue)*)? ']')
    ;

string
    : value=(DIGIT | CHAR | UNICODE_CHAR | ' ' | '\t' | '<' | '>' | '|' | '(' | ')' | '?'
    | '!' | '@' | '#' | '€' | '%' | '&' | '/' | '=' | '+' | '_'
    | '-' | '*' | '^' | '.' | ',' | ':' | ';' | '['
    | ']' | '$' | '{' | '}')+
    ;

CHAR: ('a'..'z' | 'A'..'Z');
DIGIT: ('0'..'9');
UNDER_SCORE: '_';
UNICODE_CHAR: ('\u00C0'..'\u017F' | '\u0100'..'\u024F');  // Latin characters with accents
WS  : [ \t\r\n]+;