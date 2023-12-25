interface StaffInterface {
    // adăugarea unei productii în sistem
    public void addProductionSystem(Production p);
    // adăugarea unui actor în sistem
    public void addActorSystem(Actor a);
    // stergerea unei productii care a fost adăugată de el din sistem
    public void removeProductionSystem(String name);
    // stergerea unui actor care a fost adăugat de el din sistem
    public void removeActorSystem(String name);
    // actualizarea informațiilor despre o productie care a fost adăugată de el în sistem
    public void updateProduction(Production p);
    // actualizarea informațiilor despre un actor care a fost adăugat de el în sistem
    public void updateActor(Actor a);
    // rezolvarea cererilor primite de la utilizatori
    public void resolveRequest(Request r);
}