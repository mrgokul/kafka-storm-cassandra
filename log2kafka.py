
#Modified tail2kafka to use Pygtail for tracking offset

import os, sys, time
from optparse import OptionParser
from pygtail import Pygtail
import kafka

def send_to_kafka(log, producer, delay):
    while True:
        messages = []
        if not os.path.isfile(log):
            time.sleep(delay)
            continue
        for line in Pygtail(log):
            sys.stdout.write(line)
            messages.append(line)
        producer.send(messages)
        time.sleep(delay)


def main():
    parser = OptionParser()
    parser.add_option("-s","--host",dest="host",default="localhost",
                    help="kafka host")
    parser.add_option("-p","--port",dest="port",default="9092",
                    help="kafka port")
    parser.add_option("-l","--log-file",dest="logfile",default=None,
                    help="REQUIRED: Log file to tail")
    parser.add_option("-t","--topic",dest="topic",default=None,
                        help="REQUIRED: Topic to send to")
    parser.add_option("-d","--delay",dest="delay",default=0.5,
                        help="tail delay between iterations")
    if options.topic is None or options.logfile is None:
            parser.print_help()
            sys.exit(1)

    producer = kafka.producer.Producer(options.topic,options.host,int(options.port))
    try:
        send_to_kafka(options.log, producer, options.delay)
    except KeyboardInterrupt,e:
        pass
if __name__ == '__main__':
        main()
