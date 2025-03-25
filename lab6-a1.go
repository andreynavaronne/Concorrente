package main

import (
	"fmt"
	"math/rand"
	"time"
)

func producer(numeros chan<- int, done chan<- bool) {
	defer close(numeros)
	rand.Seed(time.Now().UnixNano())
	for i := 0; i < 10; i++ {
		n := rand.Intn(1000)
		numeros <- n

	}
	done <- true
}

func consumer(numeros <-chan int) {
	for n := range numeros {
		if n%2 == 0 {
			fmt.Printf("%d\n", n)
		}
	}
}

func main() {
	numeros := make(chan int)
	done := make(chan bool)

	go producer(numeros, done)

	go consumer(numeros)

	<-done

	close(numeros)
}
