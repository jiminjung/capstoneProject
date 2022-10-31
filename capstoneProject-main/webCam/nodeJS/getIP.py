import urllib.request

ip = urllib.request.urlopen('https://ident.me').read().decode('utf8') # ip 동적으로 얻어옴

print(ip)
