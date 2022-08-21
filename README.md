# Smart.Socket
Smart Socket is separated into two parts; the first one is the hardware, which consists of sensors connected to the ESP32, relay and buzzer. 
The second part (this project) is the software, which contains a simple and easy-to-use mobile application, which will require a login process with a unique username and password, then add socket with unique ID, after that, socket's status is presented to the user in the app. Moreover, it will be presented with energy consumption.
The system must detect the current value, voltage value, compute the power consumption and disconnect the contacts if the current is excessive. Then sends the sensed values from all sensors to the database and responds correctly to the command from the mobile application. it also detect  and motion, then sound the buzzer and send notification to the app if there is a problem.
