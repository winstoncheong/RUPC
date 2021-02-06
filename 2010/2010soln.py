from itertools import chain
import math
import sys

if len(sys.argv) == 2:
    file = sys.argv[1]
else:
    file = 'sample.txt'

f = open(file)

def distance(x1, y1, x2, y2):
    return math.sqrt((x2-x1)**2 + (y2-y1)**2)

def excess_area(r, d):
    """r is blast radius. d is distance from center of circle to wall"""

    theta = 2*math.acos(d/r)
    area = 0.5 * r**2*(theta - math.sin(theta))
    # print('excess area', area)
    return area

def blast_area(r):
    return math.pi * r**2

class City:
    def __init__(self, x0, y0, x1, y1):
        self.x0 = x0
        self.x1 = x1
        self.y0 = y0
        self.y1 = y1
        self.damage = 0

    def set_catapult(self, c0, c1):
        self.c0 = c0
        self.c1 = c1

    def __repr__(self):
        return f'city: ({self.x0}, {self.y0}) to ({self.x1}, {self.y1})'

    def bombed(self, x, y, r):
        """Given values are where city gets bombed. 
        Returns whether catapult was destroyed."""

        # print('considering bomb: ', x, y, r)

        # calculate received damage

        # check whether lands inside
        if not(self.x0 < x < self.x1 and self.y0 < y < self.y1):
            # print('Bomb misses')
            return False 

        damage = blast_area(r)
        # remove excess
        if x - r < self.x0: # clips left wall
            # print('clips left')
            dist = x - self.x0
            # print('dist', dist)
            damage -= excess_area(r, dist)
        if x + r > self.x1: # clips right wall
            # print('clips right')
            dist = self.x1 - x
            # print('dist', dist)
            damage -= excess_area(r, dist)
        if y - r < self.y0: # clips top wall
            # print('clips top')
            dist = y - self.y0
            # print('dist', dist)
            damage -= excess_area(r, dist)
        if y + r > self.y1: # clips bottom wall
            # print('clips bottom')
            dist =  self.y1 - y
            # print('dist', dist)
            damage -= excess_area(r, dist)

        # print('total calculated damage: ', damage)

        self.damage += damage

        # check if catapult is destroyed
        if distance(x, y, self.c0, self.c1) <= r: # catapult is in blast radius
            return True
        return False



num_datasets = int(f.readline())
print(f'Analyzing {num_datasets} data sets')

for d in range(num_datasets):
    print(f' Data set {d}:')
    city1 = City(*map(int, f.readline().split()))
    city1.set_catapult(*map(int, f.readline().split()))
    city2 = City(*map(int, f.readline().split()))
    city2.set_catapult(*map(int, f.readline().split()))

    num_targets = int(f.readline())
    barrels1 = list(map(int, f.readline().split()))
    barrels2 = list(map(int, f.readline().split()))

    # This list alternates between city1's target and city2's target
    attack_list = []
    for t in range(num_targets):
        attack_list.append(list(map(int, f.readline().split())))

    # run simulation

    for i, attack in enumerate(attack_list):
        # print('Attack: ', i)
        # city1 attacks city2
        result = city2.bombed(attack[0], attack[1], barrels1[i])
        if result: # catapult destroyed
            print(f'  City 2 catapult destroyed after {i+1} shots.')
            print(f'  City 1 damage: {city1.damage} square meters.')
            print(f'  City 2 damage: {city2.damage} square meters.')
            break


        # city2 attacks city1 back
        result = city1.bombed(attack[2], attack[3], barrels2[i])
        if result: # catapult destroyed
            print(f'  City 1 catapult destroyed after {i+1} shots.')
            print(f'  City 1 damage: {city1.damage} square meters.')
            print(f'  City 2 damage: {city2.damage} square meters.')
            break

    else:
        print(f'  Neither catapult destroyed after {len(attack_list)} shots')
        print(f'  City 1 damage: {city1.damage} square meters.')
        print(f'  City 2 damage: {city2.damage} square meters.')