import os
import re

total = 0
for (dirpath, dirnames, filenames) in os.walk("./src"):
	for file in filenames:
		path = dirpath + "/" + file
		if os.path.isfile(path) and re.match(r".*\.java$", path) and not re.match(r".*//forms//.*", path):
			lines = 0
			with open(path, 'r') as f:
				lines = f.read().count('\n')
				total += lines
			print path + ":", lines
print "Total:", total
