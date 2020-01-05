"""

Mega basic hangman game fetching words from Wikipedia
To re-fetch the words delete the .word file

Zoltan Bacskai

z.bacskai.jr@gmail.com

2019.

"""
import os
import shelve
import random
import sys

from word_fetch import WordFetcher


def print_try(try_word):
    try_pstr = ' '.join(list(try_word))
    print(try_pstr)

def read_character():
    guess_char = ''
    while len(guess_char) != 1:
        guess_char = input("Enter a character. Or type quit ")
        if guess_char == 'quit':
            sys.exit()

    return guess_char

def validate_choice(guess_char, guess_word, try_word, num_fail):
    nfail = True
    for i in range(0,len(guess_word)):
        if guess_word[i] == guess_char:
            tw = list(try_word)
            tw[i] = guess_char
            try_word = ''.join(tw)
            nfail = False

    if nfail:
        num_fail+=1

    return num_fail, try_word

def game():
    with shelve.open('.words') as ws:
        if len(ws) == 0:
            ws['words'] = WordFetcher().fetch()

        guess_word = random.choice(ws['words'])

        print('SOLUTION %s' % guess_word)

        num_fail = 0;
        try_word = '_' * len(guess_word)

        while num_fail < 10 and try_word != guess_word:
            print_try(try_word)
            print('\nRemaining Tries: %s\n' % (10-num_fail))

            guess_char = read_character()
            num_fail, try_word = validate_choice(guess_char, guess_word, try_word, num_fail)

        if num_fail < 10:
            print ("Congratulations!")
        else:
            print ("Try next time")


if __name__=='__main__':
    game()




