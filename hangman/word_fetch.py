"""

Mega basic hangman game fetching words from Wikipedia

This module is responsible for fetching the words from Wikipedia
Zoltan Bacskai

z.bacskai.jr@gmail.com

2019.

"""
import re
import requests
from lxml import html

BASE_URL  = 'https://en.wikipedia.org%s'
START_URL = 'https://en.wikipedia.org/wiki/Main_Page'
PATH_HREF = '//a/@href'
PATH_TEXT = '//p/text()'

class WordFetcher(object):
    def _get_web_page(self, in_url):
        print ('Fetching words from: %s' % in_url)
        response = requests.get(in_url)
        byte_data = response.content

        return html.fromstring(byte_data)

    def _get_words(self, html_obj, word_set, already_fetched = set()):
        print ('Fetched %s out of 5000' % len(word_set))
        for txt in html_obj.xpath(PATH_TEXT):
            words = txt.split(' ')
            for w in words:
                if len(w) > 5 and re.match('^[\w]+$', w):
                    w = w.lower()
                    word_set.add(w)

        for hrf in html_obj.xpath(PATH_HREF):
            if len(word_set) > 5000:
                break

            if re.match(r'^/wiki*', hrf):
                nfetch = (BASE_URL % hrf)
                if nfetch not in already_fetched:
                    already_fetched.add(nfetch)
                    word_set = self._get_words(self._get_web_page(BASE_URL % hrf), word_set, already_fetched)

        return word_set

    def fetch(self):
        afetched = set()
        afetched.add(START_URL)
        return list(self._get_words(self._get_web_page(START_URL), set()))

if __name__=='__main__':
    w = WordFetcher()
    print(str(w.fetch()))
