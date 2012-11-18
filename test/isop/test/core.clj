(ns isop.test.core
  (:use [isop.core])
  (:use [clojure.test])
;;  (use 'clojure.pprint)
  (:require isop.core)
  (:import [isop.core Token])
)


(deftest it_can_tokenize_simple_argument
	(is (= (list (Token. :argument "argument"))
		(argumentlexer-lex (list "argument") ))
	)
)

(deftest it_can_tokenize_parameter
	(is (= (list (Token. :parameter "argument"))
		(argumentlexer-lex (list "--argument") ))
	)
)
	
(deftest it_can_tokenize_parameter_slash
	(is (= (list (Token. :parameter "argument"))
		(argumentlexer-lex (list "/argument") ))
	)
)

(deftest it_can_tokenize_parametervalue
	(is (= (list (Token. :parameter "parameter") (Token. :parametervalue "parametervalue"))
		(argumentlexer-lex (list "--parameter","parametervalue") ))
	)
)

(deftest it_can_tokenize_parametervalue_with_equals
	(is (= (list (Token. :parameter "parameter") (Token. :parametervalue "parametervalue"))
		(argumentlexer-lex (list "--parameter=parametervalue") ))
	)
)

