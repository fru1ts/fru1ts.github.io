from maze_env import Maze
from RL_agent import QLearningTable

TRANNING_TIME = 100


def update():
    for episode in range(TRANNING_TIME):  # the number of episode
        observation = env.reset()  # initialize maze
        while True:
            env.render()
            action = RL.choose_action(str(observation))  # choose action
            observation_, reward, done = env.step(action)  # interaction with environment and get the reward
            RL.learn(str(observation), action, reward, str(observation_))  # the agent learn the action and update the q_table
            observation = observation_  # update the state
            if done:  # find the terminal
                break
    # end of game
    print('game over')
    env.destroy()


if __name__ == '__main__':
    env = Maze()
    # print(env.n_actions)
    RL = QLearningTable(actions=list(range(env.n_actions)))
    env.after(100, update)
    env.mainloop()
    print(RL.q_table)
