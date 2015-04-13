(1..5).each { print "$it " }
println ''
('A'..'H').each { print "$it " }
println ''

def enum DAYS {
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT
}

def weekdays = DAYS.MON..DAYS.FRI
weekdays.each { print "$it " }
println ''

//Identifying boundaries
println "Range: ${weekdays.from}..${weekdays.to}"