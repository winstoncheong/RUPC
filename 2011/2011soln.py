from itertools import cycle
from pprint import pprint
import sys

alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
line_len = 25

if len(sys.argv) == 2:
    file = sys.argv[1]
else: 
    file = 'sample1.txt'
f = open(file)

def reduce_key(key):
    """If key is "PASS", just return it. Otherwise, return first 5 unique chars. 
    If don't have enough, pass is "ABCDE"
    """

    if key=='PASS':
        return key

    reduced = ''
    for k in key:
        if k not in reduced:
            reduced += k
    
    if len(reduced) >= 5:
        return reduced[0:5]

    return 'ABCDE'

def transpose_encode(text, key):
    """Assume the text is 25 characters"""
    key = reduce_key(key)
    sort_key = sorted(key)
    indices = [key.index(c) for c in sort_key]

    output = ''

    if len(text) < 25: # need to pad
        text += alphabet
        text = text[0:25]

    array = []

    # populate 5x5 array
    for i in range(5):
        row = []
        for j in range(5):
            row.append(text[i*5 + j])
        array.append(row)

    # read from the array in transposed form
    for col in indices:
        for i in range(5):
            output += array[i][col]

    return output

def transpose_decode(text, key):
    sort_key = sorted(key)
    indices = [sort_key.index(c) for c in key]
    output = ''
    array = []

    # populate 5x5 array
    for j in range(5):
        row = []
        for i in range(5):
            row.append(text[i*5 + j])
        array.append(row)

    # read from the array in transposed form
    for i in range(5):
        for col in indices:
            output += array[i][col]

    return output

            
def substitute(text, key, encode, i):
    """i is the start index for key. Is modded immediately for usage.
    Need to output new index for outside usage because index only increments for alphabetic characters
    """

    output = ''

    i %= len(key)
    for c in text:
        if not c.isalpha():
            output += c
            continue

        k = key[i]
        
        if k.islower():
            shift = -alphabet.index(k.upper())-1
        else:
            shift = alphabet.index(k)+1
        
        if encode:
            output += alphabet[(alphabet.index(c) + shift) % 26]
        else: # decode
            output += alphabet[(alphabet.index(c) - shift) % 26]

        i += 1 
        i %= len(key)

    return output, i

def substitute_encode(text, key, i):
    return substitute(text, key, True, i)

def substitute_decode(text, key, i):
    return substitute(text, key, False, i)

# Tests
# print(transpose_encode('ABCDEFGHIJKLMNOPQRSTUVWXY', 'SNAPE'))
# print(transpose_decode('CHMRWEJOTYBGLQVDINSXAFKPU', 'SNAPE'))

# print(transpose_encode('THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.', 'HOGWARTS'))
# print(transpose_decode('Q N SECOOMTUB JHIRFU KWXPR  AFVHZGD  LDBOTAOCEEY.E', 'HOGWARTS'))

# print(substitute_encode('THIS IS A TEST.', 'Jayne'))
# print(substitute_decode('DGJE DC Z UQND.', 'Jayne'))

num_datasets = int(f.readline())
print(f'Analysing {num_datasets} Data Sets')

for d in range(num_datasets):
    print(f' Data Set {d+1}:')
    transp_key = reduce_key(f.readline().strip())
    print(f' Transpose Keyword: {transp_key}')
    subst_key = f.readline().strip()
    print(f' Substitute Keyword: {subst_key}')

    num_lines_encode = int(f.readline())
    print(f' Enciphering {num_lines_encode} line(s):')
    index = 0
    for i in range(num_lines_encode):
        line = f.readline().rstrip()
        if transp_key != 'PASS':
            line = transpose_encode(line, transp_key)
        if subst_key != 'PASS':
            line, index = substitute_encode(line, subst_key, index) 
        print(line)

    num_lines_decode = int(f.readline())
    print(f' Deciphering {num_lines_decode} line(s):')
    index = 0
    for i in range(num_lines_decode):
        line = f.readline().rstrip()
        if subst_key != 'PASS':
            line, index = substitute_decode(line, subst_key, index)
        if transp_key != 'PASS':
            line = transpose_decode(line, transp_key)
        print(line)