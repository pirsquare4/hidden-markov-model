# hidden-markov-model
Attempts to simulate a hidden markov model for a bad machine reading sensor. The
robot has different probabilties of moving around a room, and using markov models,
this code tries to predict where the robot currently is based on some very vague sensor readings.
As time goes on, the manhattan distance of where the real robot is and where the markov model
based on the bad senor predicts that robot is is only about 2 units away (manhattan distance)
