#!/usr/bin/python
import sys
import time
import RPi.GPIO as GPIO
import requests
import Adafruit_DHT

#GPIO.setmode(GPIO.BOARD)

#GPIO.setup(13, GPIO.IN)

while 1:
    humidity, temperature = Adafruit_DHT.read_retry(Adafruit_DHT.DHT11, 4)
    if humidity is not None and temperature is not None:
        print('Temp={0:0.1f}*  Humidity={1:0.1f}%'.format(temperature, humidity))
        print "Mandando temperatura para o ORION\n"
        temp = '{0:0.1f}'.format(temperature)
        hum = '{0:0.1f}'.format(humidity)
        url = "http://35.197.21.171:1026/v1/updateContext"
        payload = ("{\"contextElements\": [{"
                "\"attributes\":["
                "{\"name\": \"temperature\", \"type\": \"float\", \"value\": \""+temp+"\"},"
                "{\"name\": \"hu\", \"type\": \"float\", \"value\": \""+hum+"\"}"
                "],"
                "\"id\": \"SalaInteligente1\","
                "\"isPattern\": \"false\","
                "\"type\": \"Sala\""
                "}], \"updateAction\": \"APPEND\"}")
        
        import json
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        r = requests.post(url, data=payload, headers=headers)
        print r.status_code
    else:
        print('Failed to get temperature reading!')

# Parse command line parameters.
##sensor_args = { '11': Adafruit_DHT.DHT11,
##                '22': Adafruit_DHT.DHT22,
##                '2302': Adafruit_DHT.AM2302 }
##if len(sys.argv) == 3 and sys.argv[1] in sensor_args:
##    sensor = sensor_args[sys.argv[1]]
##    pin = sys.argv[2]
##else:
##    print('usage: sudo ./Adafruit_DHT.py [11|22|2302] GPIOpin#')
##    print('example: sudo ./Adafruit_DHT.py 2302 4 - Read from an AM2302 connected to GPIO #4')
##    sys.exit(1)

# Try to grab a sensor reading.  Use the read_retry method which will retry up
# to 15 times to get a sensor reading (waiting 2 seconds between each retry).
#humidity, temperature = Adafruit_DHT.read_retry(Adafruit_DHT.DHT11, 4)

# Un-comment the line below to convert the temperature to Fahrenheit.
# temperature = temperature * 9/5.0 + 32

# Note that sometimes you won't get a reading and
# the results will be null (because Linux can't
# guarantee the timing of calls to read the sensor).
# If this happens try again!
#while 1:
#    humidity, temperature = Adafruit_DHT.read_retry(Adafruit_DHT.DHT11, 4)
#    if humidity is not None and temperature is not None:
#        print('Temp={0:0.1f}*  Humidity={1:0.1f}%'.format(temperature, humidity))
#    else:
#        print('Failed to get reading. Try again!')
#        sys.exit(1)
