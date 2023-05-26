# Performance

Using 3 benchmark articles:

Case 1:
- Full text
- pipeline.processToCoreDocument
- annotators: tokenize, ssplit, pos, lemma, stopword

6158, 5360,  4976


----
100 articles in 10524ms/10740ms
2000 articles in about 250s?

Case 2:
- Text split into sentences
- pipeline.annotate(list of sentences)
- annotators: tokenize, pos, lemma, stopword

7332, 4111, 4609

----
100 articles in 12716ms/12997ms
2000 articles in about 250s?

1000 articles (nytimes) in 72188ms