import javax.xml.namespace.QName;

public class LeasingCost {
    
    /* 
     * Description:
     *      This method creates an array of Vehicles objects from the given file name
     *
     * Parameters:
     *      filename: the file name containing the vehicles description
     *
     * File format:
     *      the first line of the file containing an integer representing how many vehicles will be described 
     *      in the following lines.
     *      Each line other than the first line describes one vehicle; 
     *      7 or 8 fragments of data in randomly placed order describing the vehicle (7 for gas car, 8 for electric car) in each line. 
     *      Each fragment end with the ';' character
     * 
     *   All possible fragments:
     *      type:FULETYPE;
     *          FULETPE can be gas or electric
     *      name:CARNAME;
     *          CARNAME is the name of the car
     *      due:DUEATSIGNING;
     *          DUEATSIGNING is a double number describing the dollar amount due when signing the lease
     *      length:LEASELENGTH;
     *          LEASELENGTH is an integer number describing the lease length in months
     *      monthly:MONTHLYCOST;
     *          MONTHLYCOST is a double number describing the monthly lease cost in dollar
     *      mile/unit:USAGE; 
     *          USAGE is a double describing miles the car can drive per gallon if fuel type is GAS, or miles the car can drive per kWh if fuel type is ELECTRIC
     *      allowance:MILEAGEALLOWANCE;
     *          MILEAGEALLOWANCE is an integer describing the maximum distance the car is allowed to travel per month
     *      charger:CHARGERCOST;
     *          CHARGERCOST is a double describing the cost of charger for electric cars, this fragment can only appear if the line is describing an electrical car
     *   Example of a line:
     *      type:gas; name:civic; due:1000; length:3; monthly:295; mile/unit:34; 
     *
     * Returns:
     *      this method returns an array of Vehicle objects 
     */
    public static Vehicle[] createAllVehicles(String filename) {
        StdIn.setFile(filename);

        int numberOfCars = Integer.parseInt( StdIn.readLine() );
        Vehicle[] vehicles = new Vehicle[numberOfCars];

        for ( int i = 0; i < numberOfCars; i++ ) {
            String line = StdIn.readLine();
            vehicles[i] = createVehicle(line);
        }
        return vehicles;
    }

    /* 
     * Description:
     *      This method calculates the CO2 emission given several parameters
     * Parameters:
     *      numberOfMonth: 
     *          the lease length in months
     *      usage: 
     *          miles the car can drive per gallon if fuelType is GAS, or
     *            miles the car can drive per kWh    if fuelType is ELECTRIC
     *      mileageAllowance: 
     *            mileage allowance per month
     *        co2PerUnit:
     *            kg of CO2 released with the combustion of each gallon of gasoline, or
     *            kg of CO2 are emitted to generate 1 kWh on average
     * Returns:
     *      this method returns a double representing the CO2 emission produced by the car during the lease.
     */
    public static double computeCO2(double numberOfMonth, double usage, double mileageAllowance, double co2PerUnit ){
        double miles = numberOfMonth * mileageAllowance ;
        return miles/usage*co2PerUnit;
    }

    /* 
     * Description:
     *      This method calculates the cost the fuel during the lease given several parameters
     * Parameters:
     *      numberOfMonth: 
     *          the lease length in months
     *      usage: 
     *          miles the car can drive per gallon if fuelType is GAS, or
     *            miles the car can drive per kWh    if fuelType is ELECTRIC
     *      mileageAllowance: 
     *            mileage allowance per month
     *        fuelPrice: 
     *            price of 1 kWh in cents of a dollar,  if fuelType is GAS, or
     *            price of one gallon of gas in dollars if fuelType is ELECTRIC
     * Returns:
     *      this method returns a double representing the fuel cost during the lease
     */
    public static double computeFuelCost(double numberOfMonth, double usage, double mileageAllowance, double fuelPrice){
        double miles = numberOfMonth * mileageAllowance;
        double cost = miles/usage * fuelPrice;
        return cost;
    }

    /* 
     * Description:
     *      This method calculates the cost of lease
     * Parameters:
     *      dueAtSigning: 
     *          the dollar amount due at signing the lease
     *      numberOfMonths: 
     *          lease length in months
     *      monthlyCost: 
     *            cost of the lease per month
     * Returns:
     *      this method returns a double representing the lease cost during the entire lease
     */
    public static double computeLeaseCost(double dueAtSigning, int numberOfMonths, double monthlyCost){
        
        return dueAtSigning + numberOfMonths*monthlyCost;
    }

    /* 
     * Description:
     *      This method creates and returns an Vehicle object with name, Lease, and Fuel properly populated based on the given string
     *      
     * Parameters:
     *      one string containing 7~8 fragments descrbing the 
     *   All possible fragments:
     *      type:FULETYPE;
     *          FULETPE can be gas or electric
     *      name:CARNAME;
     *          CARNAME is the name of the car
     *      due:DUEATSIGNING;
     *          DUEATSIGNING is a double number describing the dollar amount due when signing the lease
     *      length:LEASELENGTH;
     *          LEASELENGTH is an integer number describing the lease length in months
     *      monthly:MONTHLYCOST;
     *          MONTHLYCOST is a double number describing the monthly lease cost in dollar
     *      mile/unit:USAGE; 
     *          USAGE is a double describing miles the car can drive per gallon if fuel type is GAS, or miles the car can drive per kWh if fuel type is ELECTRIC
     *      allowance:MILEAGEALLOWANCE;
     *          MILEAGEALLOWANCE is an integer describing the maximum distance the car is allowed to travel per month
     *      charger:CHARGERCOST;
     *          CHARGERCOST is a double describing the cost of charger for electric cars, this fragment can only appear if the line is describing an electrical car
     *   Example of a line:
     *          type:gas.name:civic.due:1000.length:3.monthly:295.mile/unit:34.mileageAllowance:1200.
     *          monthly:238.name:Bolt.due:1000.mileageAllowance:20000.length:15.mile/unit:50.type:electric.charger:500.
     * Returns:
     *      this method creates and returns an Vehicle object with name, Lease, and Fuel properly populated
     *
     *
     */
    public static Vehicle createVehicle(String description){
       //   type:gas; name:civic; due:1000;

           //METHOD TO FIND NAME
            int nameIndex = description.indexOf("name:");
            int endNameIndex = description.indexOf(";", nameIndex);
            String name = description.substring(nameIndex+5, endNameIndex);
            

            //METHOD TO FIND FUEL
            int gasIndex = description.indexOf("type:");
            int endGasIndex = description.indexOf(";", gasIndex);
            String gasType = description.substring(gasIndex+5, endGasIndex);

            int usage = description.indexOf("mile/unit:");
            int endUsage = description.indexOf(";", usage);
            String use = description.substring(usage+10, endUsage);

            //METHOD TO FIND LEASE INFO
            int due = description.indexOf("due:");
            int endDue = description.indexOf(";", due);
            String finDue = description.substring(due + 4, endDue);
            
            int leaseLength = description.indexOf("length:");
            int endLease = description.indexOf(";", leaseLength);
            String finLease = description.substring(leaseLength+7, endLease);

            int month = description.indexOf("monthly:");
            int endMonth = description.indexOf(";", month);
            String finMonth = description.substring(month+8, endMonth);

            int allow = description.indexOf("allowance:");
            int endAllow = description.indexOf(";", allow);
            String finAllow = description.substring(allow+10, endAllow);

            Lease lease = new Lease(Double.parseDouble(finDue), Integer.parseInt(finLease), Double.parseDouble(finMonth), Integer.parseInt(finAllow));

            Fuel fuel = null;
            if(gasType.equals("gas"))
            {
                fuel = new Fuel(Double.parseDouble(use));
            }
            else if(gasType.equals("electric"))
            {
                int charge = description.indexOf("charger:");
                int endCharge = description.indexOf(";", charge);
                String charger = description.substring(charge+8, endCharge);
                fuel = new Fuel(Double.parseDouble(use), Double.parseDouble(charger));
                
            }
            
            Vehicle car = new Vehicle(name, fuel, lease);
                
        return car;
    }

    /* 
     * Description:
     *      The method calculates and assign CO2Emission, FuelCost, leastCost, of each vehicle.
     *      
     * Parameters:
     *      vehicles: 
     *          an array of Vehicle objects, initilized by getVehicles() method
     *      gasPrice: 
     *          a double representing the price of gas in dollar/gallon
     *      electricityPrice: 
     *            a double representing the price of gas in dollar/kW
     */
    public static void computeCO2EmissionsAndCost( Vehicle[] vehicles, double gasPrice, double electricityPrice ){
            for(int i =0; i < vehicles.length; i++)
            {
                if(vehicles[i].getFuel().getType() == 1) //gas
                {
                    vehicles[i].setCO2Emission(computeCO2(vehicles[i].getLease().getLeaseLength(), vehicles[i].getFuel().getUsage(), vehicles[i].getLease().getMileageAllowance(), 8.887));
                    vehicles[i].setFuelCost(computeFuelCost(vehicles[i].getLease().getLeaseLength(), vehicles[i].getFuel().getUsage(), vehicles[i].getLease().getMileageAllowance(), gasPrice));
                    vehicles[i].setTotalCost(vehicles[i].getFuelCost() + computeLeaseCost(vehicles[i].getLease().getDueAtSigning(), vehicles[i].getLease().getLeaseLength(), vehicles[i].getLease().getMonthlyCost()));

                } else if(vehicles[i].getFuel().getType() == 2){ //electric
                    
                    vehicles[i].setCO2Emission(computeCO2(vehicles[i].getLease().getLeaseLength(), vehicles[i].getFuel().getUsage(), vehicles[i].getLease().getMileageAllowance(), 0.453));
                    vehicles[i].setFuelCost(computeFuelCost(vehicles[i].getLease().getLeaseLength(), vehicles[i].getFuel().getUsage(), vehicles[i].getLease().getMileageAllowance(), electricityPrice));
                    vehicles[i].setTotalCost(vehicles[i].getFuel().getCharger() + vehicles[i].getFuelCost() + computeLeaseCost(vehicles[i].getLease().getDueAtSigning(), vehicles[i].getLease().getLeaseLength(), vehicles[i].getLease().getMonthlyCost()));
                }
                

            }
        

        }


    /**
     * How to compile:
     *     javac LeasingCost.java
     * How to run:         
     *     java LeasingCost vehicles.txt 3.85 11.0
     **/
    public static void main (String[] args) {
        String filename         = args[0];
        double gasPrice         = Double.parseDouble( args[1] );
        double electricityPrice = Double.parseDouble( args[2] );

        Vehicle[] vehicles = createAllVehicles(filename); 
        computeCO2EmissionsAndCost(vehicles, gasPrice, electricityPrice);

        for ( Vehicle v : vehicles ) 
            System.out.println(v.toString());
    }
}
