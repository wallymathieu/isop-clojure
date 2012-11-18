(ns isop.core)

(defn map-given-previous [proc previous items]
    (if (empty? items)
        ()
        (let [current (first items)]
            (cons (proc previous current)
                (map-given-previous proc current (rest items))
            )
        )
    )
)