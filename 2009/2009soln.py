import sys 

if len(sys.argv) > 1:
    file = sys.argv[1]
else: 
    file = 'sample1.txt'
f = open(file)

class Window:

    def __init__(self, x, y, width, height, zorder):
        self.left = x
        self.top = y
        self.right = x + width - 1
        self.bottom = y + height - 1
        self.zorder = zorder

    def enforce_screen(self, screenX, screenY):
        """Trim parts of window that are outside of screen"""

        if self.left < 0:
            self.left = 0
        if self.top < 0: 
            self.top = 0
        if self.right >= screenX:
            self.right = screenX - 1
        if self.bottom >= screenY:
            self.bottom = screenY - 1

def calculate_visible(windows, i):
    """Calculate visible portion of the i-th window. 
    Output coordinates of visibility or "completely obscured"
    """
    # Must not modify the actual window's dimensions! 
    # The window may be used to obscure other windows!

    window = windows[i]
    visible_left = window.left
    visible_right = window.right
    visible_top = window.top
    visible_bottom = window.bottom

    can_cover = [w for w in windows if w.zorder > window.zorder]

    for w in can_cover:
        # check if obscures left

        if (w.top <= visible_top and w.bottom >= visible_bottom):
            # possible to obscure left side or right side

            if w.left <= visible_left and w.right >= visible_left:
                # obscures left side
                visible_left = w.right + 1

            if w.left <= visible_right and w.right >= visible_right:
                # obscures right side
                visible_right = w.left - 1

        if (w.left <= visible_left and w.right >= visible_right):
            # possible to obscure top side or bottom side

            if w.top <= visible_top and w.bottom >= visible_top:
                # obscures top
                visible_top = w.bottom + 1

            if w.top <= visible_bottom and w.bottom >= visible_bottom:
                # obscures bottom
                visible_bottom = w.top - 1

    if visible_left > visible_right or visible_top > visible_bottom:
        return "completely obscured"
    return f'({visible_left}, {visible_top}) to ({visible_right}, {visible_bottom})'

num_datasets = int(f.readline())
print(f'Analyzing {num_datasets} Data sets')

for i in range(num_datasets):
    print(f' Data set {i+1}:')

    screenX, screenY = map(int, f.readline().split())
    print(f'  Screen size: {screenX} x {screenY}')

    num_windows = int(f.readline())
    print(f'  Windows: {num_windows}')

    windows = []
    # read in windows
    for j in range(num_windows):
        w = Window(*map(int, f.readline().split()))
        w.enforce_screen(screenX, screenY)
        windows.append(w)

    # output visible area of each window
    for j in range(num_windows):
        print(f'   Window {j+1}: {calculate_visible(windows, j)}')
    