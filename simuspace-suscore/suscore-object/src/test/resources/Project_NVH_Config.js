{ 
"EntityConfig": [
	{
		  "id": "91023f01-1d00-4b0f-9036-10105a589fe2",
		  "name": "Varinat_17-may",
		  "className": "de.soco.software.simuspace.suscore.data.entity.ProjectEntity",
		  "isVersionable": true,
		  "isCategorizable": false,
		  "isCustomizable": true,
		  "hasLifeCycle": true,
		  "lifeCycle":"ca836b74-d07c-4df2-8e57-c3d608523116",
		  "isMassData": true,
		  "isContainer": true,
		  "contains": [],
		  "links": [
		    "Loadcase"
		  ],
		  "customAttributes": [
		    {
		      "id": "",
		      "name": "customAtt1",
		      "type": "dropdown",
		      "values": [ "1",  "2",  "3" ],
		      "defaultValue": "1"
		    }
		  ]
		},
    {
        "id": "34c4c2f6-15f8-11e7-93ae-92361f052671",
        "name": "Loadcase",
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
