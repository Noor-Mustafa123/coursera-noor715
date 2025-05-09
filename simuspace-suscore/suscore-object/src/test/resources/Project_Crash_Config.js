{ 
"EntityConfig": [
    {
      "id": "62c4c2f6-15f8-11e7-93ae-92361f002671",
      "name": "Project",
      "className": "de.soco.software.simuspace.suscore.data.entity.ProjectEntity", 
      "isVersionable": true, 
      "isCategorizable": false,
      "isCustomizable": false,
      "customAttributes": [{
	      "id": "",
	      "name": "customAtt1",
	      "type": "dropdown",
	      "values": [ "1",  "2",  "3" ],
	      "defaultValue": "1"
	    }],
      "hasLifeCycle":true,
      "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
      "isMassData":true,
      "isContainer":true,
      "contains":["Variant"],
      "links":[]
    },
    {
      "id": "62c4c2f6-15f8-11e7-93ae-92361f002672",
      "name": "Variant",
      "className": "de.soco.software.simuspace.suscore.data.entity.VariantEntity",
      "isVersionable": true, 
      "isCategorizable": false,
      "isCustomizable": false,
      "customAttributes": [{
	      "id": "",
	      "name": "customAtt1",
	      "type": "dropdown",
	      "values": [ "1",  "2",  "3" ],
	      "defaultValue": "1"
	    }],
      "hasLifeCycle":true,
      "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
      "isMassData":true,
      "isContainer":true,
      "contains":["Report","List<DataObject>"],
      "links":[]
    },
    {
        "id": "62c4c2f6-15f8-11e7-93ae-92361f002673",
        "name": "Report",
        "className": "de.soco.software.simuspace.suscore.data.entity.ProjectEntity",
        "isVersionable": true, 
        "isCategorizable": false,
        "isCustomizable": false,
        "customAttributes": [{
		      "id": "",
		      "name": "customAtt1",
		      "type": "dropdown",
		      "values": [ "1",  "2",  "3" ],
		      "defaultValue": "1"
		    }],
        "hasLifeCycle":true,
        "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
        "isMassData":true,
        "isContainer":true,
        "contains":[],
        "links":[]
      },
      {
          "id": "62c4c2f6-15f8-11e7-93ae-92361f002674",
          "name": "DataObject",
          "className": "de.soco.software.simuspace.suscore.data.entity.ProjectEntity",
          "isVersionable": true, 
          "isCategorizable": false,
          "isCustomizable": false,
          "customAttributes": [{
		      "id": "",
		      "name": "customAtt1",
		      "type": "dropdown",
		      "values": [ "1",  "2",  "3" ],
		      "defaultValue": "1"
		    }],
          "hasLifeCycle":true,
          "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
          "isMassData":true,
          "isContainer":true,
          "contains":[],
          "links":[]
        }
  ]
}
