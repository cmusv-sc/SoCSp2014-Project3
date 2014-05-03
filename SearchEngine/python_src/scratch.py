#! /usr/bin/env python

import datetime
from pymongo import *
import time
import re

import config

if __name__ == '__main__':
  db = MongoClient()[config.DB_NAME]
  mashups = db[config.MASHUP_COLLECTION_NAME]
  apis = db[config.API_COLLECTION_NAME]
  api2tags = {}

  for m in mashups.find():
    api_tags = m['apis'].values()
    for a in api_tags:
      if a not in api2tags:
        api2tags[a] = []
      temp_lst = api2tags[a]
      if isinstance(api_tags, list):
        temp_lst.extend(api_tags)
      else:
        temp_list.append(api_tags)
      api2tags[a] = temp_lst

  for a in apis.find():
    name = a['link']
#    name = re.search('/.+?(/.+?$)', name).groups()[0]
    rating = a['rating']
    timestamp = a['updated']
    timestamp = str(time.mktime(datetime.datetime.strptime(timestamp, "%Y-%m-%dT%H:%M:%SZ").timetuple()))
    comment = a['description'].replace("\n","")
    tags = ''
    try:
      tags = ','.join(api2tags[name])
    except:
      pass
    try:
      print config.FILE_DELIMITER.join([name, comment, rating, tags, timestamp])
    except:
      pass
