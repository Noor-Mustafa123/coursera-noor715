#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Feb 10 11:48:07 2022

@author: sces150
"""

import json
import os
import argparse
import logging
from configparser import ConfigParser
try:
    from collections.abc import Mapping
except ImportError:
    from collections import Mapping 

class Config:
    def __init__(self):
        self.karaf = "apache-karaf-current"
        
    # Repalce basevalue in configuration path
    def replaceBaseValue(self, configs, baseValue):
        configs = str(configs)
        conf = configs.replace("{base_value}", baseValue)
        conf = conf.replace("'", '"') #converst to json object format
        return conf
    
    # Update a nested dictionary, override dstFileConfigs with newConfigs
    def dictUpdate(self, dstFileConfigs, newConfigs):
        for key, value in newConfigs.items():
            if isinstance(value, Mapping) and value:
                returned = self.dictUpdate(dstFileConfigs.get(key, {}), value)
                dstFileConfigs[key] = returned
            else:
                dstFileConfigs[key] = newConfigs[key]
        return dstFileConfigs
    
    # Merge all key value pairs
    def mergeConfigurations(self, newConfigs):
        mergedConfigs = {}
        for newConfig in newConfigs:
            config = json.loads(newConfig)
            mergedConfigs.update(config)
        return json.dumps(mergedConfigs)
    
    # Replace newConfigs in dstFile
    def replaceConfigurations(self, newConfigs, dstFile, file_type='.json'):  
        newConfigs = json.loads(newConfigs)
        with open(dstFile) as dstFileData:  
            if(file_type =='.json'):
                dstFileConfigs = json.load(dstFileData)
                dstFileConfigs = self.dictUpdate(dstFileConfigs, newConfigs)
            else:
                parser = ConfigParser()
                parser.optionxform = str
                try:
                    parser.read_string("[SuS_cfg]\n" + dstFileData.read())
                except:
                    parser.read_string("\n" + dstFileData.read())
                dstFileConfigs = dict(parser.items('SuS_cfg'))
                dstFileConfigs.update(newConfigs)
                allConfigs = ""
                for k,v in dstFileConfigs.items():
                    allConfigs += ('{}={}\n'.format(k,v))
        with open(dstFile, 'w', encoding='utf-8') as dstFileData:   
            if(file_type =='.json'):
                json.dump(dstFileConfigs, dstFileData, ensure_ascii=False, indent=2)
            else:
                dstFileData.write(allConfigs)
        return True
    
    def getConfFilesPath(self, installationPath, jsonKey):
        if (jsonKey == "schmes_algo"):
            jsonPath = configData[jsonKey]["json_path"]
            confFilesPath = os.path.join(installationPath, self.karaf, jsonPath)
        else:
            confFilesPath = os.path.join(installationPath, self.karaf, jsonKey)
        return confFilesPath
    
    # Get new configuartion from a Json File and update Configuartions in an existing file.
    def updateConfigurations(self, configFiles, confFilesPath, jsonKey):
        for fileName in configFiles:
            if(fileName.endswith('.json') | fileName.endswith('.cfg') | fileName.endswith('.conf')):
                try:
                    dstFile = os.path.join(confFilesPath, fileName)
                    srcFileConfigs = configData[jsonKey][fileName]
                    newConfigs = []
                    for confIndex in range(len(srcFileConfigs)):
                        newConfigs.append(self.replaceBaseValue(srcFileConfigs[confIndex]['config'], srcFileConfigs[confIndex]['base_value']))
                    finalConfigs = self.mergeConfigurations(newConfigs)
        
                    if(fileName.endswith('.json')):
                        writeConfig = self.replaceConfigurations(finalConfigs, dstFile)
                        logger.info(f'> configurations updated in {fileName}: {writeConfig}')    
                    else:                    
                        writeConfig = self.replaceConfigurations(finalConfigs, dstFile, file_type='other')
                        logger.info(f'> configurations updated in {fileName}: {writeConfig}')
                except:
                     logger.warning(f'> Skipping File: {fileName}')
                     continue
        return True

# Initialize ArgParser to get Json path from comand line
def initializeArgParser():
    parser = argparse.ArgumentParser()
    parser.add_argument("jsonPath", help="Json File Path", type=str)
    args = parser.parse_args()
    jsonPath = args.jsonPath
    return jsonPath 

# Initilaize logger to write logs
def initializeLogger(jsonPath):
    logFilePath = os.path.dirname(jsonPath)
    logFileName = os.path.join(logFilePath, "prepareInstallConfig.log")
    logging.basicConfig(filename=logFileName, format='%(asctime)s %(message)s', filemode='w')
    logger = logging.getLogger() 
    logger.setLevel(logging.DEBUG)  # Setting the threshold of logger to DEBUG
    return logger

def cehckFileExists(filePath):
    if os.path.exists(filePath):
        logger.info ("File Found at: " + filePath)
    else:
         logger.error ("File not found at: " + filePath)
         exit()

if __name__ == "__main__":
    jsonPath = initializeArgParser()
    logger = initializeLogger(jsonPath)
    logger.info("Initilaized Logger")
    cehckFileExists(jsonPath)
    
    # Reading Json File to update newConfigs 
    with open(jsonPath) as jsonFile: 
        try:
            configData = json.load(jsonFile)
        except:
            logger.error("Issue in Json file: {jsonPath}")
            exit()     
    if "installation_path" in configData:
        installationPath = configData["installation_path"]
    else:
        logger.error ("No 'installation_path' key found in provided Json : " + jsonPath)
        exit()
    config =  Config()
    if "conf" in configData:
        logger.info ("Found Conf file to update")
        jsonKey = "conf"
        confFilesPath = config.getConfFilesPath(installationPath, jsonKey)
        configFiles = configData[jsonKey].keys() # Enlist configFiles
        configUpdate = config.updateConfigurations(configFiles, confFilesPath, jsonKey)
        logger.info(f'Congurations Updated: {configUpdate}')
    elif "schmes_algo" in configData:
        logger.info ("Found Schmes_algo file to update")
        jsonKey = "schmes_algo"
        confFilesPath = config.getConfFilesPath(installationPath, jsonKey)
        configFiles = configData[jsonKey].keys() # Enlist configFiles
        configUpdate = config.updateConfigurations(configFiles, confFilesPath, jsonKey)
        logger.info(f'Congurations Updated: {configUpdate}')
    elif "wfengine" in configData:
        logger.info ("Found WFEngine file to update")
        jsonKey = "wfengine"
        confFilesPath = config.getConfFilesPath(installationPath, jsonKey)
        configFiles = configData[jsonKey].keys() # Enlist configFiles
        configUpdate = config.updateConfigurations(configFiles, confFilesPath, jsonKey)
        logger.info(f'Congurations Updated: {configUpdate}')
    else:
            logger.info('Invalid keys.')