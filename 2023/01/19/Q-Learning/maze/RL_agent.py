import numpy as np
import pandas as pd


class QLearningTable:
    def __init__(self, actions, learning_rate=0.01, reward_discount=0.9, epsilon_greedy=0.9):
        self.actions = actions
        self.learning_rate = learning_rate
        self.reward_discount = reward_discount
        self.epsilon_greedy = epsilon_greedy
        self.q_table = pd.DataFrame(columns=self.actions, dtype=np.float64)

    def choose_action(self, observation):
        self.check_state_exist(observation)
        if np.random.uniform() < self.epsilon_greedy:
            state_action = self.q_table.loc[observation, :]
            action = np.random.choice(state_action[state_action == np.max(state_action)].index)
        else:
            action = np.random.choice(self.actions)
        return action

    def learn(self, observation, action, reward, observation_):
        self.check_state_exist(observation_)
        q_predict = self.q_table.loc[observation, action]
        if observation_ != 'terminal':
            q_target = reward + self.reward_discount * self.q_table.loc[observation_, :].max()
        else:
            q_target = reward
        self.q_table.loc[observation, action] += self.learning_rate * (q_target - q_predict)

    def check_state_exist(self, state):
        if state not in self.q_table.index:
            self.q_table = self.q_table.append(
                pd.Series(
                    [0] * len(self.actions),  # 初始化为0
                    index=self.q_table.columns,  # 动作当做索引
                    name=state,
                )
            )
