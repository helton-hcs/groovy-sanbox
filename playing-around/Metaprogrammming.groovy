class Pessoa {
    String nome
    int idade
    void digaAlgo() {
        println "Oi, eu sou ${nome} e tenho ${idade} anos"
    }
    void digaNaoAlgo() {
        println "NaoAlgo"
    }
}


Pessoa.metaClass.nao = { println "Não" }
Pessoa.metaClass.invokeMethod = { String nome, parametros ->
    println "Vou chamar ${nome}"
    def metodo = delegate.class.metaClass.getMetaMethod(nome)
    def retorno = metodo.invoke(delegate, parametros)
    println "Chamei ${metodo}"
    retorno
}

def pessoa = new Pessoa(nome:"Helton", idade:23)

pessoa.digaAlgo()

/*
def metodos = ["digaAlgo", "digaNaoAlgo"]
for (item in metodos) {
    pessoa."${item}"()
}

def atributos = ["nome", "idade"]
for (item in atributos) {
    println pessoa."${item}"
}
*/

/*
pessoa.metaClass.properties.each {
    println it.name
}

pessoa.metaClass.methods.each {
    println it.name
}*/

/*pessoa.nao()

String.metaClass.quente = { println "Quente" }
String.metaClass.whoAmI = { println this.toString() }
"teste".quente()
"teste".whoAmI()

*/