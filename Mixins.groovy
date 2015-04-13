class Pessoa {
    String nome
    int idade
    void metodo1() {
        println "metodo1"
    }
}

class Alone {
    void metodoAlone() {
        println "Forever alone"
    }
}

class Juntinho {
    void metodo1() {
        println "Juntinho 1"
    }
    void metodo2() {
        println "Juntinho 2"
    }
}

Pessoa.mixin Alone, Juntinho

def pessoa = new Pessoa(nome:"Helton", idade:23)
pessoa.metodoAlone()
pessoa.metodo1()
pessoa.metodo2()