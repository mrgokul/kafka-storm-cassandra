import logging
from random import randint
from time import sleep
logging.basicConfig(format='%(levelname)s: %(asctime)s %(message)s',            
                    filename='example.log',level=logging.WARNING)
while True:
    sleep(0.1)
    rand = randint(0,2)
    if rand == 0:
        logging.warning('This is a warning message')
    if rand == 1:
        logging.error('This is an error message') 
    if rand == 2:
        logging.critical('And this is a critical message')
    i += 1