{

  //Missing elemetns "Split", "Gather"
  "elements": [{
      "name": "Input",
      "key": "wfe_input22",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah",
      "allowedFieldKeys": ["section", "text", "dropdown", "textarea", "integer", "boolean", "float", "file", "directory", "object", "vector"], //needs more validation options
      "templateFields": [{
        "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "User Input Parameters",
        "label": "_NA_",
        "help": null,
        "options": null,
        "key": "section",
        "path": null,
        "show": null,
        "required": false,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }],
      "fields": [], //user-filled field values
      "shape": "rect-green",
      "icon": "fa-fa-green",
      "isInternal": true,
      "isActive": true,
      "runMode": null, // can be [client,server, user-selected, null]
      "categories": [{
        "name": "element" // can be [element,composed,custom]
      }],
      "version": "0.0.1"

    },{
      "name": "Input",
      "key": "",
      "description": "A workflow element which provides a list of possible user-filled inputs, and generates outputs which have an outgoing connection to <applicable> to be used as input at <applicable>",
      "comments": "Specification developed by @Huzaifah",
      "allowedFieldKeys": ["section", "text", "dropdown", "textarea", "integer", "boolean", "float", "file", "directory", "object", "vector"], //needs more validation options
      "templateFields": [{
        "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "User Input Parameters",
        "label": "_NA_",
        "help": null,
        "options": null,
        "key": "section",
        "path": null,
        "show": null,
        "required": false,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }],
      "fields": [], //user-filled field values
      "shape": "rect-green",
      "icon": "fa-fa-green",
      "isInternal": true,
      "isActive": true,
      "runMode": null, // can be [client,server, user-selected, null]
      "categories": [{
        "name": "element" // can be [element,composed,custom]
      }],
      "version": "0.0.1"

    }, {
      "name": "Output",
      "key": "wfe_io",
      "description": "A workflow element which provides a list of possible outputs, an incoming connection can generate this output, while a outgoing connection can use this as input",
      "comments": "Specification developed by @Huzaifah",
      "allowedFieldKeys": ["section", "text", "dropdown", "textarea", "integer", "boolean", "float", "file", "directory", "object", "vector"], //needs more validation options
      "templateFields": [{
        "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "User Output Parameters",
        "label": "_NA_",
        "help": null,
        "options": null,
        "key": "section",
        "path": null,
        "show": null,
        "required": false,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }],
      "fields": [], //user-filled field values
      "shape": "rect-green",
      "icon": "fa-fa-green",
      "isInternal": true,
      "isActive": true,
      "runMode": null, // can be [client,server, user-selected, null]
      "categories": [{
        "name": "element" // can be [element,composed,custom]
      }],
      "version": "0.0.1"

    }, {
      "name": "Script",
      "key": "wfe_script",
      "description": "A workflow element which executes a script, by consuming inputs and producing outputs",
      "comments": "Specification developed by @Huzaifah",
      "allowedFieldKeys": [], //user cannot add more fields to this element
      "fields": [], //user-filled field values
      "templateFields": [{
        "name": "header", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "User Script",
        "label": "_NA_",
        "help": null,
        "options": null,
        "key": "section",
        "path": null,
        "show": null,
        "required": false,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }, {
        "name": "syntax", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "User Script Syntax",
        "label": "Script Syntax",
        "help": null,
        "options": ["bash", "bat", "Python", "Perl"],
        "key": "dropdown",
        "path": null,
        "show": null,
        "required": true,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }, {
        "name": "script", //can be changed by the user to give this parameter a name/key/cmd/variable
        "value": "",
        "label": "User Script",
        "help": null,
        "options": null,
        "key": "textarea",
        "path": null,
        "show": null,
        "required": true,
        "multiple": false,
        "icon": null,
        "subtitle": "",
        "url": null

      }],
      "shape": "rect-green",
      "icon": "fa-fa-green",
      "isInternal": true,
      "isActive": true,
      "runMode": "", // can be [client,server, user-selected, null]
      "categories": [{
        "name": "element" // can be [element,composed,custom]
      }],
      "version": "0.0.1"

    }

  ],
  "applicable": { //means an element as key can accept incoming connection from which other elements
    "wfe_script": ["wfe_input", "wfe_output"],
    "wfe_output": ["wfe_script"]
  }
}