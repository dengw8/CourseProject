import numpy as np
import pandas as pd
from sklearn.metrics import f1_score
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import cross_val_score, train_test_split

import helper
import pickle

def generate_feature_matrix(data):
    matrix = np.ndarray((0, 10))
    for line in data:
        matrix = np.vstack((matrix, extract_features(line)))
    return matrix
	
def generate_target_matrix(data):
    matrix = np.ndarray((0, 1))
    for line in data:
        word, phonetic = tuple(line.split(sep=":"))
        matrix = np.vstack((matrix, position_of_stress(phonetic)))
    return matrix
	
def extract_features(line):
    word, phonetic = tuple(line.split(sep=":"))
    result = [number_of_syllable(phonetic), end_with_vowel(phonetic)]

    for i in range(min(4, len(word))):
        result.append(sum([ord(j) for j in word[:i+1]]))

    while len(result) < 6:
        result.append(result[-1])
    for i in range(min(4, len(word))):
        result.append(sum([ord(j) for j in word[-(i+1):]]))

    while len(result) < 10:
        result.append(result[-1])

    return np.array(result)

def number_of_syllable(phonetic):
    return len(extract_vowels(phonetic))
				
def extract_vowels(phonetic):
    vowel_alphabet = ['A', 'E', 'I', 'O', 'U']
	sec_p_a = ['A', 'E', 'H', 'O', 'W']
	sec_p_e = ['H', 'R', 'Y']
	sec_p_i = ['H', 'Y']
	sec_p_o = ['W', 'Y']
	sec_p_u = ['H', 'W']
	ies = phonetic.split()
	s = list()
    for i in ies:
		if i.startswith('A') and i[1] in sec_p_a:
			s.append(i)
			continue
		elif i.startswith('E') and i[1] in sec_p_e:
			s.append(i)
			continue
		elif i.startswith('I') and i[1] in sec_p_i:
			s.append(i)
			continue
		elif i.startswith('O') and i[1] in sec_p_o:
			s.append(i)
			continue
		elif i.startswith('U') and i[1] in sec_p_u:
			s.append(i)
	return s

def position_of_stress(phonetic):
    vowels = extract_vowels(phonetic)
    for vowel in vowels:
        if vowel.endswith('1'):
            return vowels.index(vowel)+1
    raise Exception("no stress: "+phonetic)	

def end_with_vowel(phonetic):
    syllable = phonetic.split()
    return 1 if syllable[-1] in extract_vowels(phonetic) else 0

################# training #################

def train(data, classifier_file):# do not change the heading of the function
    with open(classifier_file, 'wb') as fd:
        X, Y = (generate_feature_matrix(data), generate_target_matrix(data).ravel())
        RF = RandomForestClassifier()
        RF.fit(X, Y)
        pickle.dump(RF, file=fd)

################# testing #################

def test(data, classifier_file):# do not change the heading of the function
    with open(classifier_file, 'rb') as fd:
        classifier = pickle.load(fd)
        X =  generate_feature_matrix(data)  
        prediction = classifier.predict(X)
        return list(map(int, prediction))
