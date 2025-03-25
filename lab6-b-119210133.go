package main

import (
	"fmt"
	"math/rand"
	"time"
)

func request(ch chan<- int, fim chan<- bool) {
	rand.Seed(time.Now().UnixNano())
	n := rand.Intn(10)
	time.Sleep(time.Duration(n) * time.Second)

	ch <- n
	fim <- true
}

func getaway(ngo, wait_n int) int {
	ch := make(chan int, ngo)
	fim := make(chan bool, ngo)

	for i := 0; i < ngo; i++ {
		go request(ch, fim)
	}

	soma := 0
	contador := 0
	for contador < wait_n {
		select {
		case n := <-ch:
			soma += n
			contador++
		case <-fim:

		}
	}

	return soma
}

func main() {
	rand.Seed(42)

	ngo := 10
	wait_n := 5
	s := getaway(ngo, wait_n)

	fmt.Printf("%d", s)
}
