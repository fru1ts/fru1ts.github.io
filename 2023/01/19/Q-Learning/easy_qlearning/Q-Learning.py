'''
简单寻宝游戏环境图大致如下
-----------|
’-‘表示路径，’|‘表示宝藏
'''

import numpy as np
import pandas as pd
import time  # 用来控制移动速度

np.random.seed(2)

N_STATES = 6  # 环境的长度
ACTIONS = ['left', 'right']  # 智能体的行为
EPSILON = 0.9  # epsilon-greedy  中的epsilon
ALPHA = 0.1  # 学习率
GAMMA = 0.9  # 折扣因子
MAX_EPSODES = 13  # 控制训练的次数
FRESH_TIME = 0.2  # 控制移动的速度


# 建立Q表
def build_q_table(n_states, actions):
    table = pd.DataFrame(
        np.zeros((n_states, len(actions))), columns=actions,
    )
    return table


def choose_action(state, q_table):
    # 选择行为算法
    state_actions = q_table.iloc[state, :]  # 取下一个状态的整行
    if (np.random.uniform() > EPSILON) or ((state_actions == 0).all()):  # 产生0~1的随机数，如果大于EPSILO或者在初始状态则随机选取动作
        action_name = np.random.choice(ACTIONS)
    else:  # 选取Q值最大的
        action_name = state_actions.idxmax()
    return action_name


# 环境反馈
def get_env_feedback(S, A):
    if A == 'right':
        if S == N_STATES - 2:
            S_ = 'terminal'
            R = 1
        else:
            S_ = S + 1
            R = 0
    else:
        R = 0
        if S == 0:
            S_ = S
        else:
            S_ = S - 1
    return S_, R


# 环境更新
def updata_env(S, episode, step_counter):
    env_list = ['-'] * (N_STATES - 1) + ['T']  # ------T
    if S == 'terminal':
        interaction = 'Episode %s:total_steps=%s' % (episode + 1, step_counter)
        print('\r{}'.format(interaction, end=''))
        time.sleep(2)
        print('\r                                     ', end='')
    else:
        env_list[S] = 'o'
        interaction = ''.join(env_list)
        print('\r{}'.format(interaction), end='')
        time.sleep(FRESH_TIME)


def QL():
    q_table = build_q_table(N_STATES, ACTIONS)
    for episode in range(MAX_EPSODES):
        S = 0
        step_counter = 0
        is_terminated = False
        updata_env(S, episode, step_counter)
        while not is_terminated:
            A = choose_action(S, q_table)
            S_, R = get_env_feedback(S, A)
            q_predict = q_table.loc[S, A]
            if S_ != 'terminal':
                q_target = R + GAMMA * q_table.iloc[S_, :].max()
            else:
                q_target = R
                is_terminated = True

            q_table.loc[S, A] += ALPHA * (q_target - q_predict)
            S = S_

            updata_env(S, episode, step_counter+1)
            step_counter += 1
    return q_table


if __name__ == '__main__':
    q_table = QL()
    print('\r\nQ-table:\n')
    print(q_table)
