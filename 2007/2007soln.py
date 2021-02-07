import sys

if len(sys.argv)==2:
    file = sys.argv[1]
else: 
    file = 'sample3.txt'

f = open(file)

num_datasets = int(f.readline())

for n in range(num_datasets):
    print(f'Data Set {n+1}:')
    num_symbols = int(f.readline())
    print(f'  Characters: {num_symbols}')

    # character => width in points
    character_widths = {}
    # add space between words
    character_widths[' '] = 4.0

    # group => group adjustment
    group_adjust = {}

    for s in range(num_symbols):
        char, width = f.readline().split()
        character_widths[char] = float(width)
    
    # groupings
    num_groupings = int(f.readline())
    print(f'  Groupings: {num_groupings}')

    for g in range(num_groupings):
        group, adjust = f.readline().split()
        group_adjust[group] = float(adjust)
    
    num_lines = int(f.readline())
    print(f'  Lines: {num_lines}')

    for lineno in range(num_lines): # for each line of text
        line = f.readline().strip()
        width = 0
        # process line 

        # start with naive width calculation
        for c in line:
            width += character_widths[c]

        # apply adjustments

        left_index = 0
        right_index = 2

        while left_index <= len(line):
            group = line[left_index: right_index]
            # grow group until cannot
            while right_index <= len(line):
                group = line[left_index: right_index]
                if group in group_adjust.keys():
                    right_index += 1
                    group = line[left_index: right_index]
                else:
                    right_index -= 1
                    group = line[left_index: right_index]
                    break

            # now we potentially have a group
            group = line[left_index: right_index]
            if len(group) > 1: # is a group
                # apply adjustment for group
                width += group_adjust[group]

                # prepare for new group search
                left_index = right_index - 1
                right_index = left_index + 2
            else: # increment position by one
                left_index += 1
                right_index = left_index + 2

            if left_index >= len(line) - 1: # at end of line
                break

        print(f'    Line: {lineno+1}: {round(width, 2)} points')
