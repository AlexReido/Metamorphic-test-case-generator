package main

import (
	"bytes"
	"encoding/csv"
	"fmt"
	"io"
	"log"
	"os"
	"strconv"
)

func print( a[] int ) {
	for i := 0; i < len( a ); i++ {
		fmt.Println( a[ i ] )
	}
}

func sort( a[] int) []int{
	fin := false
	for !fin {
		fin = true
		for i := 0; i <= len(a)-2; i++ {
			if a[i] > a[i+1] {
				fin = false
				val := a[i+1]
				a[i+1] = a[i]
				a[i] = val
			}
		}
	}
	return a
}


func main() {
	inFile, outFile := os.Args[1], os.Args[2]
	csvfile, err := os.Open(inFile)
	if err != nil {
		log.Fatalln("Couldn't open the csv file", err)
	}
	r := csv.NewReader(csvfile)
	r.FieldsPerRecord = 0
	var values[] int
	for  {

		record, err := r.Read()
		if err == io.EOF {
			break
		}
		if err, ok := err.(*csv.ParseError); ok && err.Err == csv.ErrFieldCount {
			log.Fatal(err)
		}

		fmt.Println(record[0])
		x, _ := strconv.Atoi(record[0])
		values = append(values, x)
		fmt.Println(values)
	}
	csvfile.Close()
	//print( values )
	values = sort ( values )
	//print( values )
	outfile, err := os.Create(outFile)
	if err != nil {
		log.Fatalln("Couldn't create the csv file", err)
	}
	w := csv.NewWriter(outfile)
	//var arr[] string
	arr := make([][]string, len(values))

	for i:=0; i<len(values); i++ {
		strVal := strconv.Itoa(values[i])
		//comma := ","
		var b bytes.Buffer
		b.WriteString(strVal)
		b.WriteString(",")
		strVal = b.String()
		fmt.Printf(strVal)
		arr[i] = make([]string, 1)
		arr[i][0] = strVal
	}
	if err = w.WriteAll(arr); err == nil {

	} else {
		log.Fatalln("Couldn't write to csv file", err)
	}

	if err, ok := err.(*csv.ParseError); ok && err.Err == csv.ErrFieldCount {
		log.Fatal(err)
	}


	w.Flush()
	outfile.Close()
}