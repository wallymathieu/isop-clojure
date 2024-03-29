(ns isop.argumentlexer
    (:use [isop.core])
)

(defrecord Token [type value])

(defn argumentlexer-lex [args]
    (defn map-tokens-from-raw [arg]
        (def param (re-find #"(--|/|-)([^:=]*)([:=]?)(.*)" arg))
        (if (nil? param)
            (Token. :argument arg)
            (let [p (Token. :parameter (nth param 2))]
                (if (= "" (nth param 3))
                    p
                    (list p (Token. :parametervalue (nth param 4)))
                )
            )
        )
    )

    (defn map-parametervalues-based-on-parameters [last item]
        (if (and (not (nil? last))
                 (= (:type last) :parameter)
                 (= (:type item) :argument))
            (Token. :parametervalue (:value item))
            item
        )
    )
    
    (map-given-previous map-parametervalues-based-on-parameters nil 
        (flatten (map map-tokens-from-raw args)))
)
    
