#!usr/env/bin
import sys

def scale(m, xf, yf):
    t = []
    for r in m: # list
        tr = ""
        for rc in r: # character of list
            for _ in range(yf): tr += rc # column
        for _ in range(xf): t.append(tr)
    return t


if __name__ == '__main__':
    r, c, zr, zc = map(int, sys.stdin.readline().split())
    print zr, zc
    while r > 0:
        l = sys.stdin.readline().rstrip().split()
        ls = scale(l, zr, zc) # assigned row, column
        r = r - 1
        print ls

