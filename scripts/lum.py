import time
import RPi.GPIO as GPIO
import requests

GPIO.setmode(GPIO.BOARD)

GPIO.setup(26, GPIO.IN)
#GPIO.setup(11, GPIO.IN)

try:
        while 1:
                if GPIO.input(26) == 1:
                        #GPIO.output(7, True)
                        print "Luminosidade baixa, mandando data para o ORION\n"
                        url = "http://35.197.21.171:1026/v1/updateContext"
                        payload = ("{\"contextElements\": [{"
                                        "\"attributes\":["
                                                "{\"name\": \"brightness\", \"type\": \"float\", \"value\": \"30\"}"
                                        "],"
                                        "\"id\": \"SalaInteligente1\","
                                        "\"isPattern\": \"false\","
                                        "\"type\": \"Sala\""
                                    "}], \"updateAction\": \"APPEND\"}")
                        import json
                        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
                        r = requests.post(url, data=payload, headers=headers)
                        print r.status_code
                        #print r.text

                        #url2 = "http://localhost:5000/act/1/on/1"
                        #r2 = requests.get(url2)
                        #print "Movimento, led on!!!\n"
                        time.sleep(1)
                elif GPIO.input(26) == 0:
                        #GPIO.output(7, False)
                        print "Luminosidade alta, mandando data para o ORION\n"
                        url = "http://35.197.21.171:1026/v1/updateContext"
                        payload = ("{\"contextElements\": [{"
                                        "\"attributes\":["
                                                "{\"name\": \"brightness\", \"type\": \"float\", \"value\": \"70\"}"
                                        "],"
                                        "\"id\": \"SalaInteligente1\","
                                        "\"isPattern\": \"false\","
                                        "\"type\": \"Sala\""
                                    "}], \"updateAction\": \"APPEND\"}")
                        import json
                        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
                        r = requests.post(url, data=payload, headers=headers)
                        print r.status_code
                        #print r.text

                        #url2 = "http://localhost:5000/act/1/off/1"
                        #r2 = requests.get(url2)
                        #print "Sem movimento, led off...\n"
                        time.sleep(1)
except KeyboardInterrupt:
        print "\nSai no teclado\n"
except Exception,e:
        print str(e)
finally:
        GPIO.cleanup()

