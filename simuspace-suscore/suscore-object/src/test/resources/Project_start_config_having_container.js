{ 
"entityConfig": [
    {
        "id": "62c4c2f6-15f8-11e7-93ae-92361f002675",
        "name": "Project",
        "className": "de.soco.software.simuspace.suscore.object.model.ProjectDTO", 
        "isVersionable": true, 
        "isCategorizable": false,
        "customAttributes": [{
          "data": "customatt1",
          "name": "customatt1",
          "title": "My Custom Attribute",
          "type": "select",
          "options": [ "1",  "2",  "3" ],
          "value": "1"
        }],
        "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
        "isMassData":false,
        "contains":["62c4c2f6-15f8-11e7-93ae-92361f002676","62c4c2f6-15f8-11e7-93ae-92361f002677"],
        "links":[]
    },
    {
        "id": "62c4c2f6-15f8-11e7-93ae-92361f002676",
        "name": "DataObject",
        "className": "de.soco.software.simuspace.suscore.object.model.DataObjectDTO",
        "isVersionable": true, 
        "isCategorizable": false,
        "customAttributes": [{
          "data": "customatt1",
          "name": "customatt1",
          "title": "Object Custom Attribute",
          "type": "select",
          "options": [ "3",  "2",  "1" ],
          "value": "1"
        }],
        "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
        "isMassData":true,
        "contains":[],
        "links":[]
    },
    {
        "id": "62c4c2f6-15f8-11e7-93ae-92361f002677",
        "name": "Variant",
        "className": "de.soco.software.simuspace.suscore.object.model.VariantDTO",
        "isVersionable": true, 
        "isCategorizable": false,
        "customAttributes": [{
          "data": "customatt1",
          "name": "customatt1",
          "title": "Object Custom Attribute",
          "type": "select",
          "options": [ "3",  "2",  "1" ],
          "value": "1"
        }],
        "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
        "isMassData":true,
        "contains":[],
        "links":[]
    }
    
  ]
}
